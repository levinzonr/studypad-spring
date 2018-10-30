package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Note
import org.springframework.data.repository.CrudRepository

interface NotesRepository : CrudRepository<Note, Long>