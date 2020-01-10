package com.example.bittouch.event

import okhttp3.Request
import okhttp3.Response

data class ApiLifecycleEvent(val request: Request, val response: Response, val contentResponse: String?)