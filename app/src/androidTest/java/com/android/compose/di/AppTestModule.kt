package com.android.compose.di

import android.app.Application
import androidx.room.Room
import com.android.compose.data.data_source.NoteDataBase
import com.android.compose.data.repository.NoteRepositoryImpl
import com.android.compose.domain.repository.NoteRepository
import com.android.compose.domain.use_case.AddNote
import com.android.compose.domain.use_case.DeleteNote
import com.android.compose.domain.use_case.GetNote
import com.android.compose.domain.use_case.GetNotes
import com.android.compose.domain.use_case.NoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppTestModule {

    @Provides
    @Singleton
    fun provideNoteDataBase(application: Application): NoteDataBase {
        return Room.inMemoryDatabaseBuilder(
            application,
            NoteDataBase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(dataBase: NoteDataBase): NoteRepository {
        return NoteRepositoryImpl(dataBase.noteDao)
    }

    @Provides
    @Singleton
    fun provideUseCase(repository: NoteRepository): NoteUseCase {
        return NoteUseCase(
            addNote = AddNote(repository),
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            getNote = GetNote(repository)
        )
    }

}
