package nostallin.com.nostallinbeta.util

class SourceException(val code: SourceExceptionCode) : RuntimeException() {

    enum class SourceExceptionCode {
        DOES_NOT_EXIST,
        API_ERROR
    }
}