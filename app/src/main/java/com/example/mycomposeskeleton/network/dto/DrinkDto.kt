package com.example.mycomposeskeleton.network.dto
import com.squareup.moshi.Json

sealed interface Drink{
    val idDrink: String
    val strDrink: String
    val strDrinkThumb: String

    data class CocktailSummaryDto(
        @Json(name = "idDrink")
        override val idDrink: String,
        @Json(name = "strDrink")
        override val strDrink: String,
        @Json(name = "strDrinkThumb")
        override val strDrinkThumb: String
    ) : Drink
}


sealed interface DrinkDetail {

    val idDrink: String
    val strDrink: String
    val strDrinkThumb: String
    val strInstructions: String

    val strImageSource: String?
    val strAlcoholic: String?
    val strGlass : String?
    val strCategory: String?
    val strIngredient1: String
    val strIngredient2: String?
    val strIngredient3: String?
    val strIngredient4: String?
    val strIngredient5: String?
    val strIngredient6: String?
    val strIngredient7: String?
    val strIngredient8: String?
    val strIngredient9: String?
    val strIngredient10: String?
    val strIngredient11: String?
    val strIngredient12: String?
    val strIngredient13: String?
    val strIngredient14: String?
    val strIngredient15: String?

    val strMeasure1: String?
    val strMeasure2: String?
    val strMeasure3: String?
    val strMeasure4: String?
    val strMeasure5: String?
    val strMeasure6: String?
    val strMeasure7: String?
    val strMeasure8: String?
    val strMeasure9: String?
    val strMeasure10: String?
    val strMeasure11: String?
    val strMeasure12: String?
    val strMeasure13: String?
    val strMeasure14: String?
    val strMeasure15: String?

    data class CocktailDto(
        @Json(name = "idDrink")
        override val idDrink: String,
        @Json(name = "strDrink")
        override val strDrink: String,
        @Json(name = "strDrinkAlternate")
        val strDrinkAlternate: String?,
        @Json(name = "strTags")
        val strTags: String?,
        @Json(name = "strVideo")
        val strVideo: String?,
        @Json(name = "strCategory")
        override val strCategory: String,
        @Json(name = "strIBA")
        val strIBA: String?,
        @Json(name = "strAlcoholic")
        override val strAlcoholic: String,
        @Json(name = "strGlass")
        override val strGlass: String,
        @Json(name = "strInstructions")
        override val strInstructions: String,
        @Json(name = "strInstructionsES")
        val strInstructionsES: String?,
        @Json(name = "strInstructionsDE")
        val strInstructionsDE: String?,
        @Json(name = "strInstructionsFR")
        val strInstructionsFR: String?,
        @Json(name = "strInstructionsIT")
        val strInstructionsIT: String?,
        @Json(name = "strInstructionsZH-HANS")
        val strInstructionsZHHANS: String?,
        @Json(name = "strInstructionsZH-HANT")
        val strInstructionsZHHANT: String?,
        @Json(name = "strDrinkThumb")
        override val strDrinkThumb: String,
        @Json(name = "strIngredient1")
        override val strIngredient1: String,
        @Json(name = "strIngredient2")
        override val strIngredient2: String?,
        @Json(name = "strIngredient3")
        override val strIngredient3: String?,
        @Json(name = "strIngredient4")
        override val strIngredient4: String?,
        @Json(name = "strIngredient5")
        override val strIngredient5: String?,
        @Json(name = "strIngredient6")
        override val strIngredient6: String?,
        @Json(name = "strIngredient7")
        override val strIngredient7: String?,
        @Json(name = "strIngredient8")
        override val strIngredient8: String?,
        @Json(name = "strIngredient9")
        override val strIngredient9: String?,
        @Json(name = "strIngredient10")
        override val strIngredient10: String?,
        @Json(name = "strIngredient11")
        override val strIngredient11: String?,
        @Json(name = "strIngredient12")
        override val strIngredient12: String?,
        @Json(name = "strIngredient13")
        override val strIngredient13: String?,
        @Json(name = "strIngredient14")
        override val strIngredient14: String?,
        @Json(name = "strIngredient15")
        override val strIngredient15: String?,
        @Json(name = "strMeasure1")
        override val strMeasure1: String,
        @Json(name = "strMeasure2")
        override val strMeasure2: String?,
        @Json(name = "strMeasure3")
        override val strMeasure3: String?,
        @Json(name = "strMeasure4")
        override val strMeasure4: String?,
        @Json(name = "strMeasure5")
        override val strMeasure5: String?,
        @Json(name = "strMeasure6")
        override val strMeasure6: String?,
        @Json(name = "strMeasure7")
        override val strMeasure7: String?,
        @Json(name = "strMeasure8")
        override val strMeasure8: String?,
        @Json(name = "strMeasure9")
        override val strMeasure9: String?,
        @Json(name = "strMeasure10")
        override val strMeasure10: String?,
        @Json(name = "strMeasure11")
        override val strMeasure11: String?,
        @Json(name = "strMeasure12")
        override val strMeasure12: String?,
        @Json(name = "strMeasure13")
        override val strMeasure13: String?,
        @Json(name = "strMeasure14")
        override val strMeasure14: String?,
        @Json(name = "strMeasure15")
        override val strMeasure15: String?,
        @Json(name = "strImageSource")
        override val strImageSource: String?,
        @Json(name = "strImageAttribution")
        val strImageAttribution: String?,
        @Json(name = "strCreativeCommonsConfirmed")
        val strCreativeCommonsConfirmed: String,
        @Json(name = "dateModified")
        val dateModified: String?
    ) : DrinkDetail

}

data class GetCocktailByIdDto(
    @Json(name = "drinks")
    val drinks: List<DrinkDetail.CocktailDto>
)

data class ResponseGetCocktailListDto(
    @Json(name = "drinks")
    val drinks: List<Drink.CocktailSummaryDto>
)
