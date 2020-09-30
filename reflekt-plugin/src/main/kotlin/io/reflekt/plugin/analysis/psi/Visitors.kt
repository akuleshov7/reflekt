package io.reflekt.plugin.analysis.psi

import org.jetbrains.kotlin.psi.*

fun KtElement.visitClassOrObject(filter: (KtClassOrObject) -> Boolean = { true }, body: (KtClassOrObject) -> Unit) {
    acceptChildren(object : KtDefaultVisitor() {
        override fun visitClassOrObject(classOrObject: KtClassOrObject) {
            if (filter(classOrObject)) body(classOrObject)

            super.visitClassOrObject(classOrObject)
        }
    })
}

fun KtElement.visitClass(filter: (KtClass) -> Boolean = { true }, body: (KtClass) -> Unit) {
    acceptChildren(object : KtDefaultVisitor() {
        override fun visitClass(klass: KtClass) {
            if (filter(klass)) body(klass)

            super.visitClass(klass)
        }
    })
}

fun KtElement.visitObject(filter: (KtObjectDeclaration) -> Boolean = { true }, body: (KtObjectDeclaration) -> Unit) {
    acceptChildren(object : KtDefaultVisitor() {
        override fun visitObjectDeclaration(declaration: KtObjectDeclaration) {
            if (filter(declaration)) body(declaration)

            super.visitObjectDeclaration(declaration)
        }
    })
}

fun KtElement.visitReferenceExpression(filter: (KtReferenceExpression) -> Boolean = { true }, body: (KtReferenceExpression) -> Unit) {
    acceptChildren(object : KtDefaultVisitor() {
        override fun visitReferenceExpression(expression: KtReferenceExpression) {
            if (filter(expression)) body(expression)

            super.visitReferenceExpression(expression)
        }
    })
}
