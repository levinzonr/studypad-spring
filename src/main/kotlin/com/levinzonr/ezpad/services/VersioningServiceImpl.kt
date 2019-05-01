package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.repositories.ModificationRepository
import com.levinzonr.ezpad.domain.repositories.VersionStateRepository
import com.levinzonr.ezpad.utils.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import sun.rmi.runtime.Log
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

    override fun deleteModifications(list: List<Long>) {
       list.forEach(modificationRepository::deleteById)
    }

    override fun modify(state: VersionState?, note: NoteBody, type: ModificationType, user: User?) {
        Logger.log(this, "Modification: $note, of type $type")
        state?.let { currrentVersionState ->
            val author = user ?: state.notebook.author


            // We are only initerested in notes modifications from concrete users
            Logger.log(this, "Mods: ${state.modifications.joinToString(",") { "noteId: ${it.noteId}, authorId: ${it.author.id}" }}")
            val existedModification = state.modifications.filter { it.author.id == author.id }.firstOrNull { it.noteId == note.sourceId || it.noteId == note.id }
            Logger.log(this, "Existed: ${existedModification}")

            // First time modification
            if (existedModification == null) {
                val modification = when (type) {
                    ModificationType.ADDED -> Modification.Added(noteId = note.id, title = note.title
                            ?: "", content = note.content ?: "", state = state, user = author)
                    ModificationType.UPDATED -> if (note.sourceId == null )  Modification.Added(noteId = note.id, title = note.title
                            ?: "", content = note.content ?: "", state = state, user = author) else  Modification.Updated(note.sourceId, note.title ?: "", author, note.content
                            ?: "", state)
                    else -> null
                }

                 modification?.let(modificationRepository::save)
            } else {

                if (existedModification is Modification.Added) {
                    println("Exited modification :$existedModification")
                    // If its a modifcation created by user, thats now is deleted - delete ic completetylly
                    if (type == ModificationType.DELETED) {
                        modificationRepository.deleteById(existedModification.id!!)
                    } else {
                        val newModification = Modification.Added(existedModification.id, existedModification.noteId!!, note.title ?: "", author, note.content ?: "", state)
                        modificationRepository.save(newModification)
                    }


                } else if (existedModification is Modification.Updated && type == ModificationType.DELETED) {
                    modificationRepository.delete(existedModification)
                    val nextModification = Modification.Deleted(existedModification.noteId!!, author, state)
                    modificationRepository.save(nextModification)
                } else {
                    val next =  Modification.Updated(note.sourceId!!, note.title ?: "", author, note.content
                            ?: "", state)
                    next.id = existedModification.id
                    modificationRepository.save(next)

                }

            }

        }
    }

    override fun applyModifications(state: VersionState?, list: List<Long>) : VersionState {
        val oldState = state ?: throw Exception()
        val remains = oldState.modifications.filterNot { list.contains(it.id) }
        Logger.log(this, remains.joinToString(",") { it.noteId.toString()  })
        val newState = oldState.copy(
                version = oldState.version + 1,
                modifications = oldState.modifications.filterNot { list.contains(it.id) })
        return versionStateRepository.save(newState)
    }

    override fun getModifications(notebookId: String): List<Modification> {
        return modificationRepository.findAll().also { println("Find ALL: $it.") }.also {
        }.filter { it.state.notebook?.id == notebookId }
    }

    override fun getModificationsByIds(list: List<Long>) : List<Modification> {
        return modificationRepository.findAllById(list).toList()
    }
}
