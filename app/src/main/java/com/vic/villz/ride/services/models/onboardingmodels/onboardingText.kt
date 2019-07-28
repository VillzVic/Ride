package com.vic.villz.ride.services.models.onboardingmodels

class OnboardingText(var header:String, var description:String)

class OnboardingTexts{
    companion object{

        var text1 = OnboardingText("Track your driver", "Always know where your driver is whenever you request for a pickup.")
        var text2 = OnboardingText("Tour the cities", "Move through different parts of your city in comfort.")
        var text3 = OnboardingText("No cash, no problem", "Payment is made easy by paying with your debit card.")

        val onboardingTextList = arrayOf(text1, text2, text3)

        fun getOnboardingTexts() = onboardingTextList
    }
}