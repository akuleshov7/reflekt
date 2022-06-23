package org.jetbrains.reflekt.plugin.ir

import com.intellij.testFramework.TestDataPath
import org.jetbrains.kotlin.test.TestMetadata
import org.jetbrains.kotlin.test.builders.TestConfigurationBuilder
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives
import org.jetbrains.kotlin.test.frontend.fir.FirFailingTestSuppressor
import org.jetbrains.kotlin.test.frontend.fir.handlers.FirIdenticalChecker
import org.jetbrains.kotlin.test.runners.AbstractKotlinCompilerTest
import org.jetbrains.kotlin.test.runners.baseFirDiagnosticTestConfiguration
import org.jetbrains.kotlin.test.services.fir.FirOldFrontendMetaConfigurator
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag("ir")
@TestMetadata("reflekt-plugin/testData/box")
@TestDataPath("/Users/Elena.Lyulina/IdeaProjects/reflekt/")
class BoxTest : AbstractKotlinCompilerTest() {


    @Test
    @TestMetadata("HelloWorld.kt")
    fun simpleTest() {
        runTest("reflekt-plugin/testData/box/HelloWorld.kt")
    }

    override fun TestConfigurationBuilder.configuration() {
        baseFirDiagnosticTestConfiguration()

        defaultDirectives {
            +FirDiagnosticsDirectives.ENABLE_PLUGIN_PHASES
        }

//        useConfigurators(::ParcelizeEnvironmentConfigurator.bind(true))

        useAfterAnalysisCheckers(
            ::FirIdenticalChecker,
            ::FirFailingTestSuppressor,
        )
        useMetaTestConfigurators(::FirOldFrontendMetaConfigurator)
    }
}
