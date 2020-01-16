### AttributeMap、AttributeKey<T>、Attribute<T>

Netty 应用程序通常与组织的专有软件集成在一起，而像 Channel 这样的组件可能甚至会在
正常的 Netty 生命周期之外被使用。在某些常用的属性和数据不可用时，Netty 提供了
AttributeMap 抽象（一个由 Channel 和引导类提供的集合）以及 AttributeKey<T>（一
个用于插入和获取属性值的泛型类）。使用这些工具，便可以安全地将任何类型的数据项与客户
端和服务器 Channel（包含 ServerChannel 的子 Channel）相关联了。

例如，考虑一个用于跟踪用户和 Channel 之间的关系的服务器应用程序。这可以通过将用户的 ID 存储为 Channel 的一个属性来完成。
类似的技术可以被用来基于用户的 ID 将消息路由给用户，或者关闭活动较少的 Channel。
