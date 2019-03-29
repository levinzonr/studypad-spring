package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.repositories.ModificationRepository
import com.levinzonr.ezpad.domain.repositories.VersionStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.Exception

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

    override fun initPublishedVersion(publishedNotebook: PublishedNotebook) : VersionState {
        val state = VersionState(notebook = publishedNotebook, version = 1)
        return versionStateRepository.save(state)
    }


    override fun modify(state: VersionState?, note: NoteBody, type: ModificationType, user: User?) {
        println("Modification: $note, of type $type")
        state?.let { currrentVersionState ->
            val author = user ?: state.notebook.author


            // We are only initerested in notes modifications from concrete users
            val existedModification = state.modifications.filter { it.author.id == user?.id }.firstOrNull { it.noteId == note.id }

            // First time modification
            if (existedModification == null) {
                val modification = when (type) {
                    ModificationType.ADDED -> Modification.Added(noteId = note.id, title = note.title
                            ?: "", content = note.content ?: "", state = state, user = author)
                    ModificationType.DELETED -> Modification.Deleted(noteId = note.sourceId!!, state = state, user = author)
                    ModificationType.UPDATED -> Modification.Updated(note.sourceId!!, note.title ?: "", author, note.content
                            ?: "", state)
                }

                 modificationRepository.save(modification)
            } else {

                if (existedModification is Modification.Added) {

                    // If its a modifcation created by user, thats now is deleted - delete ic completetylly
                    if (type == ModificationType.DELETED) {
                        modificationRepository.deleteById(existedModification.id!!)
                    } else {
                        val newModification = Modification.Added(existedModification.noteId!!, note.title ?: "", author, note.content ?: "", state)
                        modificationRepository.save(newModification)
                    }


                } else if (existedModification is Modification.Updated && type == ModificationType.DELETED) {
                    val nextModification = Modification.Deleted(existedModification.noteId!!, author, state)
                    modificationRepository.save(nextModification)
                } else {

                }

            }

        }
    }

    override fun applyModifications(state: VersionState?, list: List<Long>) : VersionState {
        val oldState = state ?: throw Exception()
        val newState = oldState.copy(
                version = oldState.version + 1,
                modifications = oldState.modifications.filterNot { list.contains(it.id) })
        return versionStateRepository.save(newState)
    }

    override fun getModifications(notebookId: String): List<Modification> {
        return modificationRepository.findAll().also { println("Find ALL: $it.") }.filter { it.state.notebook.id == notebookId }
    }

    override fun getModificationsByIds(list: List<Long>) : List<Modification> {
        return modificationRepository.findAllById(list).toList()
    }
}
