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
 - [x] a project
 - [ ] and instruction for the app run with console (showed below).

**_[Gson library](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5)_** shall be used for work with **_JSON_**.  
**_Maven or Gradle_** shall be used for dependency injection. 

#Instruction 

## App start
1. Download the project
2. Open in ``Intellij IDEA``
3. Run ``src/main/java/view/ConsoleRun.java``

## Objectives
In this app you can:
1. Create new writer. After creation, it will be stored in ``writer.json`` file.
2. Change writer's name or delete writer
3. Create new post with tags. After creation, it will be stored in ``post.json`` and ``writer.json`` (in writer object) files.
4. Anything else what you can found in submenus (presented below) 

## Menus plan 
You can find it in ``src/main/java/view`` package 
or look at ``ViewsPlan.mmd``, ``ViewsPlan.md`` or ``ViewsPlan.png`` in the root package.




From author: It is my first console crud app. It was several times rewrote.
Here is many logical mistakes in app 'architecture'.
Some methods can be described but never be used. 

The main mistake is storage list of posts in ``writers.json`` and list of tags list in ``posts.json`` instead of their id.
But it works :)




