package io.reflekt.plugin.generator

import io.reflekt.plugin.analysis.psi.isAnnotatedWith
import io.reflekt.plugin.analysis.psi.isSubtypeOf
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext

fun getInvokedElements(analyzer: ((KtClassOrObject, BindingContext) -> Boolean) -> Set<KtClassOrObject>,
                       filter: (KtClassOrObject, BindingContext) -> Boolean,
                       asSuffix: String) = analyzer(filter).joinToString { "${it.fqName.toString()}$asSuffix" }

// TODO-birillo: rename, indents
fun getWhenBodyForInvokes(fqNameList: Set<String>,
                          analyzer: ((KtClassOrObject, BindingContext) -> Boolean) -> Set<KtClassOrObject>,
                          asSuffix: String): String {
    val builder = StringBuilder()
    val filter = { fqName: String -> { clsOrObj: KtClassOrObject, ctx: BindingContext -> (KtClassOrObject::isSubtypeOf)(clsOrObj, setOf(fqName), ctx) } }
    //language=kotlin
    builder.append("""
                    ${fqNameList.map { "\"$it\" -> listOf(${getInvokedElements(analyzer, filter(it), asSuffix)})" }
                        .joinToString(separator = "\n") { it }}
                """)
    return builder.toString()
}

// TODO-birillo: rename, indents
fun getWhenBodyForInvokes(fqNamesMap: MutableMap<String, MutableList<String>>,
                          analyzer: ((KtClassOrObject, BindingContext) -> Boolean) -> Set<KtClassOrObject>,
                          asSuffix: String): String {
    val builder = StringBuilder()
    fqNamesMap.forEach { (withSubtypeFqName, fqNameList) ->
        val filterSubType = { clsOrObj: KtClassOrObject, ctx: BindingContext -> (KtClassOrObject::isSubtypeOf)(clsOrObj, setOf(withSubtypeFqName), ctx) }
        val filterWithAnnotation = { fqName: String -> { clsOrObj: KtClassOrObject, ctx: BindingContext -> (KtClassOrObject::isAnnotatedWith)(clsOrObj, setOf(fqName), ctx) } }
        val filter = { fqName: String -> { clsOrObj: KtClassOrObject, ctx: BindingContext -> filterWithAnnotation(fqName)(clsOrObj, ctx) && filterSubType(clsOrObj, ctx) } }

        //language=kotlin
        builder.append("""
                        "$withSubtypeFqName" -> {
                            when (fqName) {
                                ${fqNameList.map { "\"$it\" -> listOf(${getInvokedElements(analyzer, filter(it), asSuffix)})" }.joinToString(separator = "\n") { it }}
                                else -> error("Unknown fqName")
                            }
                        }
                    """)
    }
    return builder.toString()
}