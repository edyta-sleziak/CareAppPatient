package org.wit.careapp.models

interface AccountInfoStore {
    fun add(accountInfo: AccountInfoModel)
    fun getAccountInfo(id: Long): AccountInfoModel?
}