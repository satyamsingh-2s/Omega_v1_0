package com.example.omega_v1_0.omega_engines.layout_engine.api

import com.example.omega_v1_0.omega_engines.layout_engine.model.LayoutModel

// - it says what a layout engine do
interface LayoutEngine<T> {

    fun calculate(
        input: T,
        config: LayoutConfig
    ): LayoutModel

}