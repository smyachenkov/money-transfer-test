package com.revolut.transfer.exception

import java.lang.RuntimeException

class AccountNotFoundException(val id: Int) : RuntimeException()

class InsufficientAmountException: RuntimeException()

class InvalidTransferParams(message : String): RuntimeException(message)
