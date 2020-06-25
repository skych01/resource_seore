# 文件资源存储
以api的方式提供文件存储服务，将文件存储信息以内存哈希表的方式存储，并提供持久化机制，提供可视化展示已存储文件信息。
## 1.服务启动
启动前先配置config目录下的fileConfig.properties,里面设置了默认资源仓储位置，默认是windows环境下d:\resource目录，并且默认不开启缓存。

```
#是否开启redis缓存
environment.redis=false
# 本地存储路径 windows环境
environment.path= D:\\resource
# 本地存储路径 liunx
#environment.path= /resource
```
2.配置最大文件限制(可选)

默认是100M

配置没问题后以传统springboot项目的方式运行。
## 2.存储说明
项目运行后，访问项目根目录（端口号:8085）即可查看swagger提供的接口文档说明。

### 1.存储
接口地址：
**ip+端口/upload** 

参数 **project：用于区分项目，file：存储的文件。**

返回结果： **路径** 

### 2.访问

通过请求**ip+端口/路径**  即可访问此文件

### 3.资源存储信息查询
访问 **ip+端口/query** 即可

## 3.持久化

项目关闭之后资源以及资源信息并不会丢失，重新启动后访问 /query 发现资源信息依然存在，是因为提供了持久化的机制，在每次启动服务后会进行加载，如果项目需要转移，比如从开发环境转移到生产环境，只需要转移配置的仓库目录。


## 4.示例
java代码示例：

```
    public String store(File file) {
        RestTemplate restTemplate = new RestTemplate();

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        //设置请求体，注意是LinkedMultiValueMap
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", fileSystemResource);
        form.add("project", "ftj");

        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        String s = restTemplate.postForObject(apiProperties.getFileStoreUrl() + "/upload", files, String.class);
        return s;
    }
```

