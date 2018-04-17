package ca.allanwang.kit

import org.gradle.api.Plugin
import org.gradle.api.Project

class KitPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("kit", Versions)
        project.extensions.create("kitDependency", Dependencies)
    }

}