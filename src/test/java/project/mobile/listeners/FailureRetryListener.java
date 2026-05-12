package project.mobile.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class FailureRetryListener implements IAnnotationTransformer {

    //Overriding the transform method to set the RetryAnalyzer
    public void transform(ITestAnnotation testAnnotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {

        testAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
