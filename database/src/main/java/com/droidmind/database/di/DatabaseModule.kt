package com.droidmind.database.di

import android.content.Context
import com.droidmind.database.DroidMindDatabase
import com.droidmind.database.SecurityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSecurityManager(): SecurityManager {
        return SecurityManager()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        securityManager: SecurityManager
    ): DroidMindDatabase {
        return DroidMindDatabase.getDatabase(context, securityManager)
    }

    @Provides
    fun provideAdLogDao(database: DroidMindDatabase) = database.adLogDao()
}
