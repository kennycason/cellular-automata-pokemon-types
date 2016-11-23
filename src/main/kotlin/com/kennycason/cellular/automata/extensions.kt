package com.kennycason.cellular.automata

/**
 * Created by kenny on 11/23/16.
 */
inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int,
                                   noinline innerInit: (Int)->INNER): Array<Array<INNER>> = Array(sizeOuter) { Array<INNER>(sizeInner, innerInit) }
