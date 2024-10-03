package hhplus.ch2.architecture.lecture.domain.enumeration;

public enum LectureCapacity {

    DEFAULT(30),
    ;

    private final long value;

    LectureCapacity(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }
}
