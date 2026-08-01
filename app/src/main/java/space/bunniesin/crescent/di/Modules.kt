package space.bunniesin.crescent.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.nav.AppNavigator
import space.bunniesin.crescent.nav.AppNavigatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoatModule {
    @Singleton
    @Provides
    fun provideStoat(): ApiClient = ApiClient()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindAppNavigator(
        navigatorImpl: AppNavigatorImpl
    ): AppNavigator
}
