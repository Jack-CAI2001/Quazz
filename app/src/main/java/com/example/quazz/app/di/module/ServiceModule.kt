package com.example.quazz.app.di.module

import com.example.quazz.app.source.network.service.AccountService
import com.example.quazz.app.source.network.service.UserService
import com.example.quazz.app.source.network.service.impl.AccountServiceImpl
import com.example.quazz.app.source.network.service.impl.UserServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideUserService(impl: UserServiceImpl): UserService
}