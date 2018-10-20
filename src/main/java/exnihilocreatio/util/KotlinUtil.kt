package exnihilocreatio.util

inline fun <T> doInline(test: () -> T): T {
    return test()
}