package com.example.bookreader.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Book (

    @SerialName("kind"       ) var kind       : String?          = "",
    @SerialName("totalItems" ) var totalItems : Int?             = 0,
    @SerialName("items"      ) var items      : ArrayList<Items> = arrayListOf()

)


@Serializable
data class IndustryIdentifiers (

    @SerialName("type"       ) var type       : String? = "",
    @SerialName("identifier" ) var identifier : String? = ""

)

@Serializable
data class ReadingModes (

    @SerialName("text"  ) var text  : Boolean? = false,
    @SerialName("image" ) var image : Boolean? = false

)

@Serializable
data class PanelizationSummary (

    @SerialName("containsEpubBubbles"  ) var containsEpubBubbles  : Boolean? = false,
    @SerialName("containsImageBubbles" ) var containsImageBubbles : Boolean? = false

)

@Serializable
data class ImageLinks (

    @SerialName("smallThumbnail" ) var smallThumbnail : String? = "",
    @SerialName("thumbnail"      ) var thumbnail      : String? = ""

)

@Serializable
data class VolumeInfo (

    @SerialName("title"               ) var title               : String?                        = "",
    @SerialName("authors"             ) var authors             : ArrayList<String>              = arrayListOf(),
    @SerialName("publisher"           ) var publisher           : String?                        = "",
    @SerialName("publishedDate"       ) var publishedDate       : String?                        = "",
    @SerialName("description"         ) var description         : String?                        = "",
    @SerialName("industryIdentifiers" ) var industryIdentifiers : ArrayList<IndustryIdentifiers> = arrayListOf(),
    @SerialName("readingModes"        ) var readingModes        : ReadingModes?                  = ReadingModes(),
    @SerialName("pageCount"           ) var pageCount           : Int?                           = 0,
    @SerialName("printType"           ) var printType           : String?                        = "",
    @SerialName("categories"          ) var categories          : ArrayList<String>              = arrayListOf(),
    @SerialName("averageRating"       ) var averageRating       : Float?                           = 0f,
    @SerialName("ratingsCount"        ) var ratingsCount        : Int?                           = 0,
    @SerialName("maturityRating"      ) var maturityRating      : String?                        = "",
    @SerialName("allowAnonLogging"    ) var allowAnonLogging    : Boolean?                       = false,
    @SerialName("contentVersion"      ) var contentVersion      : String?                        = "",
    @SerialName("panelizationSummary" ) var panelizationSummary : PanelizationSummary?           = PanelizationSummary(),
    @SerialName("imageLinks"          ) var imageLinks          : ImageLinks?                    = ImageLinks(),
    @SerialName("language"            ) var language            : String?                        = "",
    @SerialName("previewLink"         ) var previewLink         : String?                        = "",
    @SerialName("infoLink"            ) var infoLink            : String?                        = "",
    @SerialName("canonicalVolumeLink" ) var canonicalVolumeLink : String?                        = ""

)

@Serializable
data class ListPrice(

    @SerialName("amount"       ) var amount: Float = 0f,
    @SerialName("currencyCode" ) var currencyCode: String? = ""

)

@Serializable
data class RetailPrice(

    @SerialName("amount"       ) var amount: Float = 0f,
    @SerialName("currencyCode" ) var currencyCode: String? = ""

)


@Serializable
data class Offers (

    @SerialName("finskyOfferType" ) var finskyOfferType : Int?         = 0,
    @SerialName("listPrice"       ) var listPrice       : ListPrice?   = ListPrice(),
    @SerialName("retailPrice"     ) var retailPrice     : RetailPrice? = RetailPrice()

)

@Serializable
data class SaleInfo (

    @SerialName("country"     ) var country     : String?           = "",
    @SerialName("saleability" ) var saleability : String?           = "",
    @SerialName("isEbook"     ) var isEbook     : Boolean?          = false,
    @SerialName("listPrice"   ) var listPrice   : ListPrice?        = ListPrice(),
    @SerialName("retailPrice" ) var retailPrice : RetailPrice?      = RetailPrice(),
    @SerialName("buyLink"     ) var buyLink     : String?           = "",
    @SerialName("offers"      ) var offers      : ArrayList<Offers> = arrayListOf()

)

@Serializable
data class Epub (

    @SerialName("isAvailable" ) var isAvailable : Boolean? = false

)

@Serializable
data class Pdf (

    @SerialName("isAvailable"  ) var isAvailable  : Boolean? = false,
    @SerialName("acsTokenLink" ) var acsTokenLink : String?  = ""

)


@Serializable
data class AccessInfo (

    @SerialName("country"                ) var country                : String?  = "",
    @SerialName("viewability"            ) var viewability            : String?  = "",
    @SerialName("embeddable"             ) var embeddable             : Boolean? = false,
    @SerialName("publicDomain"           ) var publicDomain           : Boolean? = false,
    @SerialName("textToSpeechPermission" ) var textToSpeechPermission : String?  = "",
    @SerialName("epub"                   ) var epub                   : Epub?    = Epub(),
    @SerialName("pdf"                    ) var pdf                    : Pdf?     = Pdf(),
    @SerialName("webReaderLink"          ) var webReaderLink          : String?  = "",
    @SerialName("accessViewStatus"       ) var accessViewStatus       : String?  = "",
    @SerialName("quoteSharingAllowed"    ) var quoteSharingAllowed    : Boolean? = false

)

@Serializable
data class SearchInfo (

    @SerialName("textSnippet" ) var textSnippet : String? = ""

)

@Serializable
data class Items (

    @SerialName("kind"       ) var kind       : String?     = "",
    @SerialName("id"         ) var id         : String?     = "",
    @SerialName("etag"       ) var etag       : String?     = "",
    @SerialName("selfLink"   ) var selfLink   : String?     = "",
    @SerialName("volumeInfo" ) var volumeInfo : VolumeInfo? = VolumeInfo(),
    @SerialName("saleInfo"   ) var saleInfo   : SaleInfo?   = SaleInfo(),
    @SerialName("accessInfo" ) var accessInfo : AccessInfo? = AccessInfo(),
    @SerialName("searchInfo" ) var searchInfo : SearchInfo? = SearchInfo()

)