package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.PublishedNote
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PublishedNotebookRepository : CrudRepository<PublishedNotebook, String>

interface PublishedNoteRepository: CrudRepository<PublishedNote, Long>