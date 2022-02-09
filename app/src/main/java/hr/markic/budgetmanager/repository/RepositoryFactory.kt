package hr.markic.budgetmanager.repository

class RepositoryFactory {

    companion object Factory {
        fun createRepository(): AppRepository = AppRepository()
    }
}