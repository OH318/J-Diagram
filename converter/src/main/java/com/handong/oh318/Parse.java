package com.handong.oh318;

import java.util.ArrayList;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public interface Parse {

    ArrayList<JavaClassSource> getJavaClassSources(String path);
}
