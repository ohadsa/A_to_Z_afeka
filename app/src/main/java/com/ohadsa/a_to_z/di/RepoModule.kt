package com.ohadsa.a_to_z.di

import com.ohadsa.a_to_z.network.RemoteApi
import com.ohadsa.a_to_z.network.RemoteApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteApi(remoteApiImpl: RemoteApiImpl): RemoteApi
}