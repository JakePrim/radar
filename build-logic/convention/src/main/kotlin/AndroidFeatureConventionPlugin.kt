import com.android.build.gradle.LibraryExtension
import com.appn.android.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/**
 * Feature 公约插件
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                //基础的core插件
                apply("appn.android.core")
                //kotlin json 序列化插件
                apply("kotlinx-serialization")
            }

            dependencies {
                //引入基础的core
                add("implementation", project(":core:core-common"))
                add("implementation", project(":core:core-network"))
                add("implementation", project(":core:core-utils"))
                add("implementation", project(":core:core-ui"))
                add("implementation", project(":core:core-router"))
                add("implementation", project(":core:core-image"))
                //引入基础的依赖项
                add("implementation", libs.findLibrary("material").get())
                add("implementation", libs.findLibrary("constraintlayout").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
                add("testImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.ext.junit").get())
                add("androidTestImplementation", libs.findLibrary("espresso.core").get())
                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))
            }
        }
    }
}