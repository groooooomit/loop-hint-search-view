package com.bfu.loophintsearchview

import org.junit.Test
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PriorityQueueTest {

    class TimedComparator<T>(
        private val originComparator: Comparator<T>?
    ) : Comparator<TimedValue<T>> {

        override fun compare(o1: TimedValue<T>, o2: TimedValue<T>): Int = originComparator
            ?.compare(o1.value, o2.value)
            ?.takeIf { it != 0 }
            ?: (o1.id compareTo o2.id)

    }


    data class TimedValue<T>(val value: T) {

        val id = idGenerator.incrementAndGet()

        companion object {
            private val idGenerator = AtomicLong(0)
        }
    }


    @Test
    fun `test same priority`() {
        // 根据 enqueue 时间来判断优先级
        val queue = PriorityQueue<TimedValue<Int>>() { a, b -> 0 }
//        (0..10).forEach {
//        queue.add(1)
//        queue.add(4)
//        queue.add(2)
//        queue.add(3)
//        queue.add(7)
//        queue.add(5)
//        }
        val list = queue.indices.map {
            queue.poll()
        }
        println(list)
    }

}