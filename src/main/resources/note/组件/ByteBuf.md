## ByteBuf——Netty的数据容器

Netty的ByteBuffer替代品是ByteBuf

Netty的数据处理API通过ByteBuf和ByteBufHolder暴露

ByteBuf API的优点：
+ 它可以被用户自定义的缓冲区类型扩展
+ 通过内置的复合缓冲区类型实现了透明的零拷贝
+ 容量按需增长
+ 读写模式切换不需要flip()方法
+ 读写使用了不同的索引
+ 支持方法的链式调用
+ 支持引用计数
+ 支持池化

ByteBuf维护了两个不同的索引：一个用于读取，一个用于写入。

读写索引的起始位置都是0。

ByteBuf的默认大小限制为Integer.MAX_VALUE。

### ByteBuf的使用模式

#### 1、堆缓冲区

数据存储在JVM的堆空间。这种模式也被称为支撑数组。适合于有遗留数据需要处理的情况。

#### 2、直接缓冲区

直接缓冲区的内容驻留在堆之外。直接缓冲区对于网络数据传输是理想的选择。堆缓冲区的数据发送之前都会先拷贝到直接缓冲区。

直接内存：不经过JVM内存直接访问系统物理内存，零拷贝所在。

堆内存：运行时数据区域

直接内存读写比堆内存快，但是他的创建、销毁更慢。

#### 3、复合缓冲区

多个ByteBuf提供一个聚合视图

CompositeByteBuf实现了这个模式

### ByteBuf分配

#### 1、按需分配：ByteBufAllocator接口

为了降低分配和释放内存的开销，Netty通过该接口实现ByteBuf池化，分配任意类型的ByteBuf实例。

可以通过Channel或者ChannelHandlerContext获取一个ByteBufAllocator引用。

pooled——池化

netty提供了两种实现：PooledByteBufAllocator和UnpooledByteBufAllocator，前者池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片，后者没有池化，每次调用都会返回一个新的实例。

Netty4.1.x默认使用PooledByteBufAllocator。

Netty4.0.x默认使用UnpooledByteBufAllocator。

#### 2、Unpooled缓冲区

某些情况下无法获取一个ByteBufAllocator引用。

Unpooled提供了静态方法来创建未池化的ByteBuf实例。

### 引用计数——ReferenceCounted接口

通过在某个对象所持有的资源不再被其他对象引用时释放该对象所持有的资源来优化内存使用和性能的技术。

