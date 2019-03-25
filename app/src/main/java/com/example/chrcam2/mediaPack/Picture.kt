package com.example.chrcam2.mediaPack

class Picture(){
    fun writeText() : String {
        return "hello this is Picture"
    }
}



/*
abstract class Vehicle(val name: String,
                       val color: String,
                       val weight: Double) {   // Concrete (Non Abstract) Properties

    // Abstract Property (Must be overridden by Subclasses)
    abstract var maxSpeed: Double

    // Abstract Methods (Must be implemented by Subclasses)
    abstract fun start()
    abstract fun stop()

    // Concrete (Non Abstract) Method
    fun displayDetails() {
        println("Name: $name, Color: $color, Weight: $weight, Max Speed: $maxSpeed")
    }
}
*/