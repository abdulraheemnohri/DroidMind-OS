package com.droidmind.core.ai.di

import android.content.Context
import com.droidmind.core.ai.AIEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @Provides
    @Singleton
    fun provideAIEngine(@ApplicationContext context: Context): AIEngine {
        return AIEngine(context)
    }
}
