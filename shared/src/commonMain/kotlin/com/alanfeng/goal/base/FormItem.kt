package com.alanfeng.goal.base

data class FormItem<T>(val data:T, val required:Boolean=false, val hint:Boolean=false)
