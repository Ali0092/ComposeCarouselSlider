package com.example.composecarousellib.internal

import androidx.compose.foundation.pager.PagerState
import kotlin.math.absoluteValue

/**
 * Signed offset of [page] from the current settled page, including drag fraction.
 * Range: negative when [page] is to the left of the target, positive when to the right.
 */
fun PagerState.signedPageOffset(page: Int): Float =
    (page - currentPage) - currentPageOffsetFraction

/**
 * Absolute distance of [page] from the current page including drag fraction.
 */
fun PagerState.absPageOffset(page: Int): Float =
    signedPageOffset(page).absoluteValue
