package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.repositories.ModificationRepository
import com.levinzonr.ezpad.domain.repositories.VersionStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VersioningServiceImpl : VersioningService {

    @Autowired
    private lateinit var modificationRepository: ModificationRepository

    @Autowired
    private lateinit var versionStateRepository: VersionStateRepository

    override fun initLocalVersion(source: PublishedNotebook, notebook: Notebook) : VersionState {

        // Reset previuouse state
        notebook.state?.let {
            modificationRepository.deleteAll(it.modifications)
            versionStateRepository.delete(it)
        }

        val state = VersionState(notebook = notebook, version = source.state?.version ?: 1)
        return versionStateRepository.save(state)
    }

    override fun initPublishedVersion(publishedNotebook: PublishedNotebook) {
        val state = VersionState(notebook = publishedNotebook, version = 1)
        versionStateRepository.save(state)
    }


    override fun modify(state: VersionState?, note: Note, type: ModificationType) {
        state?.let { currrentVersionState ->

            val existedModification = modificationRepository.findAll().firstOrNull { it.id == note.id }


            // First time modification
            if (existedModification == null) {
                val modification = when (type) {
                    ModificationType.ADDED -> Modification.Added(noteId = note.id!!, title = note.title
                            ?: "", content = note.content ?: "", state = state)
                    ModificationType.DELETED -> Modification.Deleted(noteId = note.id!!, state = state)
                    ModificationType.UPDATED -> Modification.Updated(note.id!!, note.title ?: "", note.content
                            ?: "", state)
                }

                 modificationRepository.save(modification)
            } else {

                if (existedModification is Modification.Added) {

                    // If its a modifcation created by user, thats now is deleted - delete ic completetylly
                    if (type == ModificationType.DELETED) {
                        modificationRepository.deleteById(existedModification.id)
                    } else {
                        val newModification = Modification.Added(existedModification.id, existedModification.title, existedModification.content, state)
                        modificationRepository.save(newModification)
                    }


                } else if (existedModification is Modification.Updated && type == ModificationType.DELETED) {
                    val nextModification = Modification.Deleted(existedModification.noteId, state)
                    modificationRepository.save(nextModification)
                } else {

                }

            }

        }
    }

    override fun getModifications(notebookId: String): List<Modification> {
        return modificationRepository.findAll().filter { it.state.notebook.id == notebookId }
    }
}
