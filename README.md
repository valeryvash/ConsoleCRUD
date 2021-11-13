 #Task

Need to realize a console **_CRUD_** app, which contains the next entities:
```java
Writer (id,name,List<Post> posts)
Post(id,content,List<Tag> tags, PostStatus status)
Tag(id,name)
PostStatus(enum ACTIVE,DELETED)
```

Entities shall be stored in the next text files:

`writers.json`, `posts.json`, `tags.json`

User shall be able to create, to get, to update and to delete data with console commands.

Layers:
- `model` - **_POJO_** classes
- `repository` - classes for access to the text files
- `controller` - classes for user requests 
- `view` - all data for console work

Examples are: `Writer`, `WriterRepository`, `WriterController`, `WriterView`, etc.

Basic interface shall be used for repository layer:

```java
interface GenericRepository<T, ID>;
interface WriterRepository extends GenericRepository<Writer,Long>
class GsonWriterRepositoryImpl implements WriterRepository
```
Task result shall be a separate repository with a README.md file, which contains:

 - [x] a task description,
 - [ ] a project (in progress)
 - [ ] and instruction for the app run with console.

**_[Gson library](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5)_** shall be used for work with **_JSON_**.  
**_Maven or Gradle_** shall be used for dependency injection. 

#Instruction 



