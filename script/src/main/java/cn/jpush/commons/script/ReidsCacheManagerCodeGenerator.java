package cn.jpush.commons.script;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.LocalVariableAttribute;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 生成redis cache manager的工具
 * Created by leolin on 16/3/3.
 */

public class ReidsCacheManagerCodeGenerator {
    private static ClassPool pool = ClassPool.getDefault();

    public static void main(String[] args) {
        String basePath = ReidsCacheManagerCodeGenerator.class.getResource("/").getPath();

        File javaFile = toJavaFilename(new File(basePath), "out", "RedisCacheManager.java");

        Configuration cfg = new Configuration();
        try {
            // 步骤一：指定 模板文件从何处加载的数据源，这里设置一个文件目录
            cfg.setDirectoryForTemplateLoading(new File(ReidsCacheManagerCodeGenerator.class.getClassLoader().getResource(".").getPath()));
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            // 步骤二：获取 模板文件
            Template template = cfg.getTemplate("redisManager.ftl");

            // 步骤三：创建 数据模型
            Map<String, Object> root = createDataModel();

            // 步骤四：合并 模板 和 数据模型
            // 创建.java类文件
            if(javaFile != null){
                Writer javaWriter = new FileWriter(javaFile);
                template.process(root, javaWriter);
                javaWriter.flush();
                System.out.println("文件生成路径：" + javaFile.getCanonicalPath());

                javaWriter.close();
            }
            // 输出到Console控制台
            Writer out = new OutputStreamWriter(System.out);
            template.process(root, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    /**
     * 创建数据模型
     */
    private static Map<String, Object> createDataModel() {
        try {
            final CtMethod[] methods = pool.get(Jedis.class.getName()).getDeclaredMethods();
            final CtMethod[] interfaceMethods = pool.get(JedisCommands.class.getName()).getDeclaredMethods();
            //只实现这个接口的方法
            final Set<String> signatures = new HashSet<String>(){{
                for(CtMethod method : interfaceMethods){
                    this.add(method.getSignature() + method.getName());
                }
            }};
            return new HashMap<String, Object>(){{
                this.put("methods", new LinkedList<Object>(){{

                    for (final CtMethod method : methods) {
                        //判断是否非静态方法，并且属于JedisCommon接口
                        if(Modifier.isStatic(method.getModifiers()) || !signatures.contains(method.getSignature() + method.getName())){
                            continue;
                        }
                        this.add(new HashMap<String, Object>() {{

                            this.put("returnType", getWrapperClassName(method.getReturnType()));
                            this.put("name", method.getName());
                            this.put("args", new LinkedList<Object>() {{

                                final CtClass[] types = method.getParameterTypes();
                                final LocalVariableAttribute params = (LocalVariableAttribute) method.getMethodInfo2().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
                                for (int i = 0; i < types.length; i++) {
                                    final int index = i;
                                    this.add(new HashMap<String, String>() {{
                                        this.put("type", getClassName(types[index]));
                                        this.put("name", params.variableName(index + 1));
                                    }});
                                }

                            }});

                        }});
                    }

                }});
            }};
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建.java文件所在路径 和 返回.java文件File对象
     * @param outDirFile 生成文件路径
     * @param javaPackage java包名
     * @param javaClassName java类名
     */
    private static File toJavaFilename(File outDirFile, String javaPackage, String javaClassName) {
        String packageSubPath = javaPackage.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        File file = new File(packagePath, javaClassName);
        if(!packagePath.exists()){
            //noinspection ResultOfMethodCallIgnored
            packagePath.mkdirs();
        }
        return file;
    }

    /**
     * 基础类型不能作为泛型，需要的时候通过这个方法获得包装类
     * @param c class
     * @return 包装类名
     */
    private static String getWrapperClassName(CtClass c){
        String name = getClassName(c);
        if(name.equals("int"))
            name = "Integer";
        else if(name.equals("long"))
            name = "Long";
        else if(name.equals("char"))
            name = "Character";
        else if(name.equals("boolean"))
            name = "Boolean";
        else if(name.equals("short"))
            name = "Short";
        else if(name.equals("byte"))
            name = "Byte";
        else if(name.equals("double"))
            name = "Double";
        else if(name.equals("float"))
            name = "Float";
        return name;
    }

    /**
     * 基础类型不能作为泛型，需要的时候通过这个方法获得包装类
     * @param c class
     * @return 包装类名
     */
    private static String getClassName(CtClass c){
        String name = c.getName().replaceAll("\\$", ".");
        name = name.replaceFirst("^java\\.lang\\.", "");
        return name;
    }

}
