package space.foxmount.redfox.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import space.foxmount.redfox.data.api.ApiRepository
import space.foxmount.redfox.data.db.AppDatabase
import space.foxmount.redfox.data.db.PostDao
import space.foxmount.redfox.data.repository.RedditRepository
import javax.inject.Inject
import javax.inject.Singleton

@Module
class AppModule(val mApplication: Application) {
    @Provides
    @Singleton
    internal fun providesApplication(): Application {
        return mApplication
    }
}

@Module
class ContextModule(val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }
}

@Module
class PreferenceModule {

    @Singleton
    @Provides
    @Inject
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preference", Context.MODE_PRIVATE)
    }
}

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideApi(): ApiRepository {
        return ApiRepository.getInstance()
    }
}

@Module
class RoomModule @Inject constructor(app: Application) {
    val demoDatabase: AppDatabase


    init {
        demoDatabase = Room.databaseBuilder(
            app, AppDatabase::class.java, "user_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(app: Application): AppDatabase {
        return demoDatabase
    }

    @Singleton
    @Provides
    fun providesUserDao(demoDatabase: AppDatabase): PostDao {
        return demoDatabase.postDao()
    }

    @Singleton
    @Provides
    fun productRepository(productDao: PostDao, api: ApiRepository): RedditRepository {
        return RedditRepository(api, productDao)
    }

}
