package com.tranthephong.fooddeliveryapp.Util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object RememberPrefs {
    private const val PREF = "fd_prefs"
    private const val KEY_REMEMBER = "remember_me"
    private const val KEY_ONBOARDING = "onboarding_done"

    fun setRemember(ctx: Context, value: Boolean) =
        ctx.getSharedPreferences(PREF, MODE_PRIVATE).edit().putBoolean(KEY_REMEMBER, value).apply()

    fun shouldAutoLogin(ctx: Context): Boolean =
        ctx.getSharedPreferences(PREF, MODE_PRIVATE).getBoolean(KEY_REMEMBER, false)

    fun setOnboardingDone(ctx: Context) =
        ctx.getSharedPreferences(PREF, MODE_PRIVATE).edit().putBoolean(KEY_ONBOARDING, true).apply()

    fun isFirstLaunch(ctx: Context): Boolean =
        !ctx.getSharedPreferences(PREF, MODE_PRIVATE).getBoolean(KEY_ONBOARDING, false)
}