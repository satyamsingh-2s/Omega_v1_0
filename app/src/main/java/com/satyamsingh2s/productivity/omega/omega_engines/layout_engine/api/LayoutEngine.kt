package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.api

import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.model.LayoutModel

// - it says what a layout engine do
interface LayoutEngine<T> {

    fun calculate(
        input: T,
        config: LayoutConfig
    ): LayoutModel

}