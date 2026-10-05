package com.shindoclient.injection.transformer

import org.lwjgl.opengl.GLContext
import org.lwjgl.system.FunctionProvider
import java.lang.reflect.Method
import java.nio.ByteBuffer

class Lwjgl2FunctionProvider : FunctionProvider {
    private val m_getFunctionAddress: Method

    init {
        try {
            m_getFunctionAddress = GLContext::class.java.getDeclaredMethod("getFunctionAddress", String::class.java)
            m_getFunctionAddress.isAccessible = true
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getFunctionAddress(functionName: CharSequence): Long {
        try {
            return m_getFunctionAddress.invoke(null, functionName.toString()) as Long
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getFunctionAddress(byteBuffer: ByteBuffer): Long {
        throw UnsupportedOperationException()
    }
}