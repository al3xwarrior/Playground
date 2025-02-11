package com.al3x.housing2.MineSkin

data class SkinData(
    val uuid: String,
    val name: String?,
    val texture: String
)

data class BiggerSkinData(
    val uuid: String,
    val name: String?,
    val texture: TextureData?
)

data class BiggerSkinResponse(
    val success: Boolean,
    val skin: BiggerSkinData
)

data class TextureData(
    val data: DataData,
    val hash: HashData,
    val url: UrlData
)

data class DataData(
    val value: String,
    val signature: String
)

data class HashData(
    val skin: String
)

data class UrlData(
    val skin: String
)

data class SkinResponse(
    val success: Boolean,
    val skins: List<SkinData>
)