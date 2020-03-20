# 1 Container basics
## Cloud Native是什么
```
Cloud native technologies empower organizations to build and run scalable applications in modern, dynamic environments such as public, private, and hybrid clouds. Containers, service meshes, microservices, immutable infrastructure, and declarative APIs exemplify this approach.

These techniques enable loosely coupled systems that are resilient, manageable, and observable. Combined with robust automation, they allow engineers to make high-impact changes frequently and predictably with minimal toil.
```
* Microservices
* DevOps
* CICD
* Containers
```
自动化部署、伸缩和操作应用程序容器的开源平台
应用部署，规划，更新，维护的一种机制
Docker 项目给 PaaS 世界带来的“降维打击”，其实是提供了一种非常便利的打包机制。这种机制直接打包了应用运行所需要的整个操作系统，从而保证了本地环境和云端环境的高度一致，避免了用户通过“试错”来匹配两种不同运行环境之间差异的痛苦过程
Kubernetes 项目最主要的设计思想是，从更宏观的角度，以统一的方式来定义任务之间的各种关系，并且为将来支持更多种类的关系留有余地。
```
* 云原生是一条最佳路径
  - 低心智负担的
  - 敏捷的
  - 可扩展可复制额

## kubernetes是什么
Kubernetes is an oepn-source system for automating deployment, 
scaling, and management of containerized applications.

## 1.1 隔离与限制
* 容器的本质是一种特殊的进程,视图被隔离资源受限制的进程
  - 容器里的pid=1 的进程就是应用本身
  - kubernetes就是云时代的操作系统
  - 以此类推,容器镜像就是这个操作系统的软件安装包
  - pod = "进程组"
* Namespace 技术实际上修改了应用进程看待整个计算机“视图”，即它的“视线”被操作系统做了限制，只能“看到”某些指定的内容
* 容器之间使用的就还是同一个宿主机的操作系统内核
* 容器是一个“单进程”模型
* Cgroups 资源隔离
  - 一个子系统目录加上一组资源限制文件的组合来实现资源隔离

## 1.2 详解容器镜像
* rootfs（根文件系统）
* 与虚拟机的区别
  - 不需要Hypervisor和guestOs，所以轻量
  - 性能消耗低
  - 缺点: 隔离的不彻底，对于宿主机的OS的依赖和安全隐患
* 核心原理
  - 启用 Linux Namespace 配置
  - 设置指定的 Cgroups 参数
  - 切换进程的根目录（Change Root）
* 由于 rootfs 里打包的不只是应用，而是整个操作系统的文件和目录，也就意味着，应用以及它运行所需要的所有依赖，都被封装在了一起
* 分层镜像
* Docker Volume
  - docker run -v /home:/test 


# 2 Kubernetes Architechture
* Master(4个逻辑组件)
  - apiserver 
    - 提供了资源操作的唯一入口，并提供认证、授权、访问控制、API注册和发现等机制
    - 唯一和etcd联系的组件
    - 声明式API
  - controller manager 
    - 负责维护集群的状态，比如故障检测、自动扩展、滚动更新等
    - Deployment
  - scheduler 
    - 负责资源的调度，按照预定的调度策略将Pod调度到相应的机器上
  - etcd
    - 保存了整个集群的状态
    - 分布式存储系统
    - 高可用
* Node
  - kubelet 
    - 负责维护容器的生命周期，同时也负责Volume（CVI）和网络（CNI）的管理
    - node & docket mtrics
    - request
    - pods和它们上面的容器
    - images镜像
    - volumes、etc
    - 唯一不是通过容器管理的服务
  - kube-proxy
    - 负责为Service提供cluster内部的服务发现和负载均衡, 
    - 通过iptable实现
  - Container runtime 
    - 负责镜像管理以及Pod和容器的真正运行（CRI)
* Addons
  - Storage Plugin
  - Network Plugin
    - kube-dns负责为整个集群提供DNS服务
    - Ingress Controller为服务提供外网入口
  - WebUI
    - Dashboard提供GUI
  - level login
  - Heapster提供资源监控
  - Federation提供跨可用区的集群
  - Fluentd-elasticsearch提供集群日志采集、存储与查询

## 2.1 API Server
* 只接受json格式的数据

## 2.2 Scheduler
### 2.2.1 调度策略
* Etcd <=> ApiServer <=> Scheduler
* 流程
  - 1.预选策略(Predicate)
  - 2.优选策略(Priority)
  - 3.banding
* 具体使用
  - affinity(亲和性)
  - Antiaffinity
  - 污点与污点容忍 kubetcl taint nodes nodename:NoSchedule
* 调度失败错误列表
  – https://github.com/kubernetes/kubernetes/blob/release-1.9/plugin/pkg/scheduler/algorithm/predicates/error.go#L25-L58


### 2.2.2 亲和性调度
* nodeAffinity: Node亲和性调度
  - 用于替换 NodeSelector到调度策略
  - 引入运算符 In NotIn...
  - 支持枚举label
  - 硬性过滤 preferredDuringSchedulingIgnoredDuringExecution: 必须满足指定规则
  - 软性评分 requiredDuringSchedulingIgnoredDuringExecution: 优先满足指定规则
  - 硬性过滤支持多条件之间的逻辑或运算
  - 软性评分支持条件设置权重
  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: with-node-affinity
  spec:
    affinity:
      nodeAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
          nodeSelectorTerms:
          - matchExpressions:
            - key: beta.kubernetes.io/arch
              operator: In
              values:
              - amd64
        preferredDuringSchedulingIgnoredDuringExecution:
        - weight: 1
          preference:
            matchExpressions:
            - key: disktype
            operator: NotIn
            values:
            - ssd
    containers:
    - name: with-node-affinity
      image: gcr.io/google_containers/pause:2.0
  ```

* podAffinity: Pod亲和与互斥调度策略
  - 与节点不同的是,Pod属于某一个命名空间
  - 定义在pod.spec中, labelSelector的匹配对象为pod
  - podAntiAffinity
  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: pod-affinity
  spec:
    affinity:
      podAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
        - labelSelector
          matchExpressions:
          - key: security
            operator: In
            values:
            - S1
        toplogKey: failure-domain.beta.kubernetes.io/zone
      podAntiAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
        - labelSelector
          matchExpressions:
          - key: app
            operator: In
            values:
            - nginx
        toplogKey: kubernetes.io/hostname
    containers:
    - name: with-pod-affinity
      image: gcr.io/google_containers/pause:2.0
  ```
* Taints 和 Tolerations (污点和容忍)
  - 让node拒绝pod,除非pod明确声明可以容忍这个Taint,否则就不会被调度到这个node
    - 硬性排斥 NoSchedule
    - 软性排斥 PreferNoSchedule
  - kubectl taint node node1 key=value:NoSchedule
  - 删除 kubectl taint node node1 key:NoSchedule-
  - 空的key配合Exists能够匹配所有的键和值
  - 空的effect匹配所有的effect
  ```
  tolerations:
  - key: "key"
    operator: "Equal"
    value: "value"
    effect: "NoSchedule"
  ```
* Pod Priority Preemption: Pod优先级调度
  - 会尝试释放目标节点上优先级最低的Pod来安置高优先级的Pod
  - 核心行为是驱逐(Eviction)与抢占(Preemption)
  ```
  apiVersion: v1
  kind: PriorityClass
  metadata:
    name: high-priority
  value: 1000000
  globalDefault: false
  description: "This priority class should be used for sss service pods only"
  ---
  apiVersion: v1
  kind: Pod
  metadata:
    name: nginx
  spec:
    containers:
    - name: nginx
      image: nginx
    priorityClassName: high-priority
  ```
* 自定义调度器

## 2.3 Controller manager
* Kubernetes 操作这些“集装箱”的逻辑，都由控制器（Controller）完成
* https://github.com/kubernetes/kubernetes/tree/master/pkg/controller
* 由声明式API驱动-k8s资源对象
* 在具体实现中，实际状态往往来自于 Kubernetes 集群本身。
* 而期望状态，一般来自于用户提交的 YAML 文件。
* cd kubernetes/pkg/controller/
* kind: Deployment 才会用到
* 管理不同资源的生命周期
* pod的生命周期
  - Pedding => containerCreating => Running => Succeded(Failed) or Ready(CrashLoopBackOff) or Unknown
* 控制循环
  ```
  for {
    实际状态 := 获取集群中对象 X 的实际状态（Actual State）
    期望状态 := 获取集群中对象 X 的期望状态（Desired State）
    if 实际状态 == 期望状态{
      什么都不做
    } else {
      执行编排动作，将实际状态调整为期望状态
    }
  }
  ```
* 控制器定义(包括期望状态) + template 被控制对象模版
  - Deployment <=> DP controller
  - ReplicaSet <=> RS controller
  - Service <=> Svc controller

## 2.4 ETCT

## 2.5 Runtime

## 2.6 Networking
* 网络栈组成
  - 网卡（Network Interface）
  - 回环设备（Loopback Device）
  - 路由表（Routing Table）
  - iptables 规则
* 容器可直接使用宿主机网络
  - $ docker run –d –net=host --name nginx-host nginx
  - 减少性能损耗
* CNI
  - flannel: 网络配置
  - calico: 网络配置, 网络策略

## 2.7 Storage
* Pod Volumes
  - 本地存储:         empty/hostpath...
  - 网络存储:         in-tree:awsElasticBlockStore, out-of-tree:flexvolume/csi
  - 云存储:           aws EBS, GCP ...
  - Projected Volume: secret/configmap/downwardAPI/serviceAccountToken
  - PV与PVC体系
  ```
  apiVersion: v1
  kind: Pod
  metaData:
    name: myapp
  spec:
    containers:
    - name: myapp
      image: nginx
      volumeMounts:
      - name: html
        mountPath:
    volumes:
    - 
  ```
* Persistent Volumes (PV)
  - PV 描述的，是一个具体的Volume的属性，比如 Volume 的类型、挂载目录、远程存储服务器地址等。
  - Static Persistent Volumes
  - Dynamic Persistent Volumes
  - 两阶段处理: Attach & Mount
  ```
  # Attach
  $ gcloud compute instances attach-disk <虚拟机名字> --disk <远程磁盘名字>
  # Mount
  # 通过lsblk命令获取磁盘设备ID
  $ sudo lsblk
  # 格式化成ext4格式
  $ sudo mkfs.ext4 -m 0 -F -E lazy_itable_init=0,lazy_journal_init=0,discard /dev/<磁盘设备ID>
  # 挂载到挂载点
  $ sudo mkdir -p /var/lib/kubelet/pods/<Pod的ID>/volumes/kubernetes.io~<Volume类型>/<Volume名字>
  ```
* PersistentVolumeClain (PVC)
  - PVC 描述的，是 Pod 想要使用的持久化存储的属性，比如存储的大小、读写权限等
* Local Persistent Volume(LPC)
* StorageClass
  - 充当PV的模版
  ```
  apiVersion: storage.k8s.io/v1
  kind: StorageClass
  metadata:
    name: block-service
  provisioner: kubernetes.io/gce-pd
  parameters:
    type: pd-ssd
  ---
  apiVersion: v1
  kind: PersistentVolumeClaim
  metadata:
    name: claim1
  spec:
    accessModes:
      - ReadWriteOnce
    storageClassName: block-service
    resources:
      requests:
        storage: 30Gi
  ```
* 存储快照
* CSI (Container Storage Interface)
  - StorageClass.provisioner字段来调用

## 2.8 kube-proxy
```
kubectl logs -n kube-system kube-proxy-pscvh
```

# 3 Kubernetes Workloads
* 资源对象 kubectl explain xx
  - workload: Pod、Deploymnt、ReplicaSet、StatefulSet、DaemonSet、Job、CronJob
  - 服务发现与均衡: Service、Ingress
  - 配置与存储: PV、PVC、ConfigMap、Secret、DownwoardAPI
  - 集群级资源: Namespace, Node, Role, ClusterRole, RoleBinding, ClusterRoleBinding
  - 元数据资源: HPA, PodTemplate, LimitRange
  - Namespace
    - 集群的隔离与共享
    - 同命名空间可通过主机名互相访问，但可以通过server ip, pod ip访问
    - kubectl get namespaces
    - 按环境划分: dev, test; 按团队划分; 自定义多级划分: lumine_dev
* yaml组成 get pod myapp -o yaml
  - apiVersion/v1: 
    - 组名/版本号，组名省略表示是核心组件, kubectl api-versions
  - kind: 
    - 资源类别，初始化具体资源对象时使用
  - metadata: 元数据
    - name: 同一类别唯一
    - namespace
    - lables
    - annotation
    - 每个资源的引用path: /api/GROUP/VERSION/namespaces/NAME_SPACE/TYPE/NAME
  - spec: 
    - 规格,最重要的对象，定义特性，然后靠控制器确保这些特性能被满足
  - status:
    - 当前状态,由k8s集群维护
  - * 列表需要加-, 映射不需要
* 控制器
  - Replication Controller(旧)
  - Replica Set
    - 作为Deployment的理想状态参数使用 
  - Deployment
    - 未来对所有长期伺服型的的业务的管理，都会通过Deployment来管理
    - pod资源配置管理
  - HorizontalPodAutoscaler
  - StatefulSet
  - Job, CronJob
  - DaemonSet
    - 保证每个Node上都运行一个pod副本
* Service
  - 一个Pod只是一个运行服务的实例
  - 通过
  - 集群内部有效的虚拟IP
* ConfigMap
  - 向pod提供非敏感的数据信息
* Secret
* PetSet
  - 区别于无状态的cattle 
  - 每个Pod挂载自己独立的存储
  - 适合数据库服务MySQL和PostgreSQL，集群化管理服务Zookeeper、etcd等有状态服务
* Federation
  - 跨Region跨服务商K8s集群服务而设计
* Node
  - Pod真正运行的主机，可以物理机，也可以是虚拟机 

## 3.1 pod
* pod
  - K8s集群中运行部署应用或服务的最小单元(微服务)
  - long-running, batch, node-daemon, stateful application
  - 所有的容器均在Pod中运行
  - 一个Pod可以承载一个或者多个相关的容器
  - 同一个Pod中的容器会部署在同一个物理机器上并且能够共享资源
  - 一旦一个Pod被创建，系统就会不停的监控Pod的健康情况以及Pod所在主机的健康情
* pod的本质
  - 容器的“单进程模型”，并不是指容器里只能运行“一个”进程，而是指容器没有管理多个进程的能力
  - Pod 看成传统环境里的“机器”、把容器看作是运行在这个“机器”里的“用户程序”
  - 凡是调度、网络、存储，以及安全相关的属性，基本上是 Pod 级别
  - Pod，其实是一组共享了某些资源的容器,pod = "进程组"
  - pod定义超亲密关系
    - 发生直接的文件交换
    - 通过localhost或socket文件进行本地通信
    - 频繁的RPC调用
    - 共享linux namespace
* 共享网络
  - Infra容器: k8s.gcr.io/pause,永远处于暂停状态,100~200kb
  - 直接使用loaclhost通信
  - 看到的网络设备跟infra容器看到的完全一样
  - 一个pod只有一个ip地址,也就是这个pod对应的NetworkNamespace对应的ip地址
  - pod的生命周期和infra容器一致
* 共享存储
* 顺序相关的容器定义为Init Container
  - 会比用户容器先启动,并严格按照定义顺序依次执行
* 容器设计模式: Sidecar
  - 通过在pod里定义专门容器，来执行主业务容器需要的辅助工作
  - 将辅助功能同主业务容器解耦，实现独立发布和能力重用
  - Sidecar: 应用与日志收集,Fluentd容器共享日志volume从而将日志转发到远程存储
  - Sidecar: 代理容器,对业务容器屏蔽被代理的服务集群,简化业务代码的实现逻辑
  - Sidecar: 适配器容器
  - 所有设计模式的本质都是解耦和复用
* 当用户想在一个容器里跑多个功能并不相关的应用时，应该优先考虑它们是不是更应该被描述成一个Pod里的多个容器

## 3.1 Pod资源
* name
* image
* imagePullPolicy
  - Always, Never, IfNotPresent
  - 本地镜像有安全风险
* 修改镜像中的默认应用
  - command, args
* 标签
  - key:value  数字, 字母, -, _, .
  - 通过标签过滤: kubectl get pods -l app=myapp
  - 打标签: kubectl label pods demo xxx=bbb
* nodeSelector
  - 通过标签选择部署的node
* nodeName
  - 制定特定node
* annotations:
  - 与label不同的是不能用于挑选资源对象，仅用于对象提供元数据
* Resources
  - 多维度集群资源管理
  - CPU, GPU, 100m(0.1核)
  - 内存 memory: 100Mi 
  - 持久化存储
  - Node <=> ApiServer
  - 核心设计
    - Requests: 希望被分配到的被保障的资源量,资源不够会处于Pending
    - Limits: 使用资源上限
    - Limits > Requests 比较可靠，CPU属于可压缩资源，所以不会被killd
  - LimitRange
* [容器设计模式](https://www.usenix.org/conference/hotcloud16/workshop-program/presentation/burns)

## 3.2 Pod 的“水平扩展 / 收缩”（horizontal scaling out/in）
* Deployment 控制器实际操纵的，正是这样的 ReplicaSet 对象，而不是 Pod 对象
* 通过ReplicaSet的个数来描述应用的版本
* 通过ReplicaSet的属性来保证pod的副本数量
* [Canary deployment](https://github.com/ContainerSolutions/k8s-deployment-strategies/tree/master/canary)
  ```
  apiVersion: apps/v1
  kind: ReplicaSet
  metadata:
    name: nginx-set
    labels:
      app: nginx
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: nginx
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
        - name: nginx
          image: nginx:1.7.9
  ```
* 扩展 3 => 4
  ```
  $ kubectl scale deployment nginx-deployment --replicas=4
  deployment.apps/nginx-deployment scaled
  ```
* 查看部署状态
  ```
  $ kubectl get deployments
  NAME               DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
  nginx-deployment   3         0         0            0           1s
  // 查看Deployment 对象状态
  $ kubectl rollout status deployment/nginx-deployment
  Waiting for rollout to finish: 2 out of 3 new replicas have been updated...
  deployment.apps/nginx-deployment successfully rolled out
  ```
* 版本回滚
  ```
  $ kubectl rollout undo deployment/nginx-deployment
  deployments "nginx-deployment"
  // 查看历史回滚到固定版本
  $ kubectl rollout history deployment/nginx-deployment
  deployments "nginx-deployment"
  REVISION    CHANGE-CAUSE
  1           kubectl create -f nginx-deployment.yaml --record
  2           kubectl edit deployment/nginx-deployment
  3           kubectl set image deployment/nginx-deployment nginx=nginx:1.91
  $ kubectl rollout history deployment/nginx-deployment --revision=2
  ```

## 3.3 Replica Set


## 3.4 Deployment
* Deployment <=> ReplicaSet <=> Pod
  - Deployment只负责管理不同版本的ReplicaSet,由ReplicaSet管理Pod副本数
* Deployment状态
  - DESIRET: 期望的pod数(replicas)
  - CURRENT: 当前实际的pod数
  - UP-TO-DATE: 达到期望版本的pod数
  - AVAILABLE: 运行中并可用的pod数
  - AGE: deployment创建的时长
* Pod名字格式 ${deployment-name}-${template-hash}-${random-suffix}
* 更新镜像
  - kubectl set image deployment.v1.apps/nginx-deployment nginx=nginx1.9.1
* 快速回滚
  - 回滚到上一版本 kubectl rollout undo deployment/nginx-deployment
  - 回滚到指定版本 kubectl rollout undo deployment/nginx-deployment --to-version=2
  - 查找历史版本   kubectl rollout history deployment/nginx-deployment
* Deployment控制器
  - ![Deployment控制器](quiver-image-url/CB63D4255F41E2B311A8B6CD1B029A90.png =877x462)
* 字段解析
  - MinReadySeconds: 判断Pod available的最小ready时间
  - revirionHistoryLimit: 保留历史版本数量
  - paused: 标识Deployment只做数量维持不做新的发布
  - progressDeadlineSeconds: 判断fail的最大时间
  - MaxUnavailable: 滚动过程中最大不可用Pod数
  - MaxSurge: 滚动过程中最多存在多少个pod超过期望replica数量

## 3.5 DaemonSet 守护进程控制器
* DaemonSet的作用
  - 保证这个Pod运行在Kubernetes集群里的每一个节点上
  - 当有新的节点加入Kubernetes集群后,该Pod会自动地在新节点上被创建出来
  - 而当旧节点被删除后,它上面的Pod也相应地会被回收掉
  - 跟踪Pod状态，保证每个节点Pod处于运行状态
* 适用场景
  - 各种网络插件Agent组件
  - 集群存储进程: glusterd, ceph
  - 日志收集进程: Fluentd, Logstash
  - 需要在每个节点运行的监控收集器
* DaemonSet比整个集群出现的时机都要早
* 例
  ```
  apiVersion: apps/v1
  kind: DaemonSet
  metadata:
    name: fluentd-elasticsearch
    namespace: kube-system
    labels:
      k8s-app: fluentd-logging
  spec:
    selector:
      matchLabels:
        name: fluentd-elasticsearch
    template:
      metadata:
        labels:
          name: fluentd-elasticsearch
      spec:
        tolerations:
        - key: node-role.kubernetes.io/master
          effect: NoSchedule
        containers:
        - name: fluentd-elasticsearch
          image: k8s.gcr.io/fluentd-elasticsearch:1.20
          resources:
            limits:
              memory: 200Mi
            requests:
              cpu: 100m
              memory: 200Mi
          volumeMounts:
          - name: varlog
            mountPath: /var/log
          - name: varlibdockercontainers
            mountPath: /var/lib/docker/containers
            readOnly: true
        terminationGracePeriodSeconds: 30
        volumes:
        - name: varlog
          hostPath:
            path: /var/log
        - name: varlibdockercontainers
          hostPath:
            path: /var/lib/docker/containers
  ```
* 更新DaemonSet
  - RollingUpdate: 默认更新策略,全自动
  - OnDelete: 

## 3.6 StatefulSet
* 首先,StatefulSet的控制器直接管理的是 Pod
* 其次,Kubernetes通过Headless Service为这些有编号的Pod，在DNS服务器中生成带有同样编号的DNS记录
* 最后，StatefulSet 还为每一个 Pod 分配并创建一个同样编号的 PVC。
* 将Pod的拓扑状态,按照Pod的“名字+编号”的方式固定了
* 拓扑状态
  * 必须按照某些顺序启动或删除
  * 任何一次pod的更新都会触发statefulset 滚动更新
* 存储状态
  * 应用的多个实列 
* 三个组件
  - headless service
  - statusfullset
  - volumeClaimTemplate
* updateStrategy
  - partition
* app-0.app.default.svc.cluster.local
```
  volumeClaimTemplates:
  - metadata:
      name: www
    spec:
      accessModes:
      - ReadWriteOnce
      resources:
        requests:
          storage: 1Gi 
```
* Service的访问
  * VIP: Virture IP
  * DNS: Normal Service || Headless Service
* Headless Service(clusterIP: None )
  ```
  apiVersion: v1
  kind: Service
  metadata:
    name: nginx
    labels:
      app: nginx
  spec:
    ports:
    - port: 80
      name: web
    clusterIP: None
    selector:
      app: nginx
  ```

## 3.7 Job & CronJob
* Pod名字格式${job-name}-${random-suffix}
* Job Controller
  ```
  apiVersion: batch/v1
  kind: Job
  metadata:
    name: pi
  spec:
    template:
      spec:
        containers:
        - name: pi
          image: resouer/ubuntu-bc 
          command: ["sh", "-c", "echo 'scale=10000; 4*a(1)' | bc -l "]
        restartPolicy: Never
    backoffLimit: 4
  ```
* job状态 kubectl get jobs
  - COMPLETIONS: 完成Pod数量
  - DURATION: Job业务实际运行时长
  - AGE: deployment创建的时长
* 并行运行job
  - completions: 一个Job在任意时间最多可以启动多少个Pod同时运行
  - parallelism: Job至少要完成的Pod数目，即Job的最小完成数
* CronJob 语法
  - schedule: crontab时间格式相同
  - startingDeadlineSeconds: Job最长启动时间
  - concurrencyPolicy: 是否允许并行运行
  - successfulJobsHistoryLimit: 允许留存历史job个数
```
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: hello
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            args:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster
          restartPolicy: OnFailure
  startingDeadlineSeconds: 10
  concurrencyPolicy: Allow
  successfulJobsHistoryLimit: 3
```

## 3.8 Init Container(初始化容器)


## 3.9 Pod健康检查和服务可用性检查
* LivenessProbe(存活探针)
  - 判断容器是否存活,即Pod状态是否为Running
  - 检查失败: 杀掉Pod
  - 适用场景: 支持重新拉起的应用
* ReadinessProbe(就绪探针)
  - 判断容器是否启动完成，即Pod的container是否为Ready
  - 检查失败: 切断上层流量到Pod
  - 适用场景: 启动后无法立即对外服务的应用
  - kubectl get pods 显示的ready判断标准
* 探测方式
  - ExecAction:      通过执行命令来检查服务是否正常，命令返回值为0则表示容器健康
  - HTTPGetAction:   通过发送http GET请求返回200~399状态码表面容器健康
  - TCPSocketAction: 通过容器的ip和Port执行TCP检查,如果能够建立TCP连接则表面容器健康
* 探测结果
  - Susses:  Container通过了检查
  - Failure: Container未通过了检查
  - Unknow:  未能执行检查(timeout,etc)，不采取任何动作
* restartPolicy
  - Always:    在任何情况下，只要容器不在运行状态，就自动重启容器
  - OnFailure: 只在容器 异常时才自动重启容器
  - Never:     从来不重启容器
* 其他参数
  - initialDelaySeconds: Pod启动后延迟多久进行检查
  - periodSeconds:       检查时间间隔，默认10秒
  - timeoutSeconds:      探测的超时时间，默认1秒
  - successThreshold:    探测失败后再次判断成功的阈值，默认为1
  - failureThreshold:    探测失败的重试次数，默认为3
* 注意事项
  - 调大判断的超时阈值，防止在容器压力较高的情况下出现偶发超时
  - 调整判断次数的阈值
  - exec如果执行的是shell脚本判断(推荐c或go编译后的二进制脚本)，在容器中调用时间会非常长
  - tcpSocket的方式遇到TLS场景，需要业务层判断是否有影响
* 容器健康检查和恢复机制 livenessProbe restartPolicy
  - 只要Pod的restartPolicy指定的策略允许重启异常的容器(比如：Always),那么这个Pod就会保持Running状态,并进行容器重启.否则Pod就会进入Failed状态
  - 对于包含多个容器的Pod,只有它里面所有的容器都进入异常状态后,Pod才会进入Failed状态.在此之前Pod都是Running状态.此时Pod的READY字段会显示正常容器的个数
* 应用故障排查
  - Pod停留在Pending
    - Pending表示调度器没有介入,可以通过kubectl describe pod,查看事件排查,通常和资源使用相关
  - Pod停留在waiting
    - 一般表示Pod镜像没有被正常拉取,通常可能和私有镜像,镜像地址不存在,镜像公网拉取相关
  - Pod不断被拉起且可以看到crashing
    - 通常表示Pod已经完成调度并启动,但是启动失败,通常由于配置,权限造成,需要查看Pod日志
  - Pod处在running但没有正常工作
    - 通常由于部分字段拼写错误造成,可以通过校验部署来排查: kubectl applyu --validate -f pod.yaml
  - Sercice无法正常工作
    - 在排查网络插件自身问题外,最可能是label配置有问题,可以通过endpoint的方式进行检查
* 应用远程调试 - Pod远程调试
  - 进入一个正在运行的Pod
    - kubectl exec -it pod-name /bin/bash
  - 进入一个正在运行包含多容器的Pod
    - kubectl exec -it pod-name -c container-name /bin/bash 
* 应用远程调试 - Service远程调试
  - 当集群中应用依赖的引用需要本地调试时
    - Telepresence --swap-deployment $DEPLOYMENT_NAME
  - 当本地开发的应用需要调用集群中的服务时
    - kubectl port-forward svc/app -n app-namespace 
* kubectl-debug
  - 非介入式调测
* 应用问题诊断的三个步骤
  - 通过describe查看状态，通过状态判断排查方向
  - 查看对象的event事件，获取更详细的信息
  - 查看Pod的日志缺的应用自身的情况
  ```
  apiVersion: v1
  kind: Pod
  metadata:
    labels:
      test: liveness
    name: test-liveness-exec
  spec:
    containers:
    - name: liveness
      image: busybox
      args:
      - /bin/sh
      - -c
      - touch /tmp/healthy; sleep 30; rm -rf /tmp/healthy; sleep 600
      livenessProbe:
        exec:
          command:
          - cat
          - /tmp/healthy
        initialDelaySeconds: 5
        periodSeconds: 5
      ...
      livenessProbe:         # HTTPGetAction
           httpGet:
             path: /healthz
             port: 8080
             httpHeaders:
             - name: X-Custom-Header
               value: Awesome
             initialDelaySeconds: 3
             periodSeconds: 3
      ...
      livenessProbe:      # TCPSocketAction
        tcpSocket:
          port: 8080
        initialDelaySeconds: 15
        periodSeconds: 20
  ```

## 3.10 Lifecycle Hooks
  * postStart并不严格保证顺序
    - 容器启动后立马执行，如果失败会销毁容器
  * preStop会阻塞当前的容器杀死流程
    ```
    apiVersion: v1
    kind: Pod
    metadata:
      name: lifecycle-demo
    spec:
      containers:
      - name: lifecycle-demo-container
        image: nginx
        lifecycle:
          postStart:
            exec:
              command: ["/bin/sh", "-c", "echo Hello from the postStart handler > /usr/share/message"]
          preStop:
            exec:
              command: ["/usr/sbin/nginx","-s","quit"]
    ```

## 3.11 ConfigMap
* 适合不用加密的信息
* 解耦容器镜像和可变配置，从而保证pod的可移植性
* 用法
  - 生成容器内的环境变量
  - 设置容器启动参数
  - 以Volume的形式挂载为容器内部的文件或目录
* 容器应用对ConfigMap的使用
  - 通过环境变量获取ConfigMap中的内容,pod不会同步更新
  - 通过Volume挂载的方式将ConfigMap中的内容挂载为容器内部的文件或目录
* kubectl create configmap [NAME][DATA]
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: cm-appvars
data:
  apploglevel: info
  appdatadir: /var/data
```
```
apiVersion: v1
kind: Pod
metadata:
  name: cm-test-pod
spec:
  containers:
  - name: cm-test
    image: busybox
    command: [ "/bin/sh", "-c", "env | grep APP" ]
    env:
    - name: APPLOGLEVEL     # 定义环境变量的名称
      valueFrom:            # key "apploglevel" 对应的值
        configMapKeyRef:
          name: cm-appvars  # 环境变量取自 cm-appvars
          key: apploglevel  # key 为 apploglevel
    - name: APPDATADIR
      valueFrom:
        configMapKeyRef:
          name: cm-appvars
          key: appdatadir
  restartPolicy: Never
```
* envFrom, 1.6开始引入,实现了在pod环境中将ConfigMap中所有定义的key=value自动生成环境变量
```
apiVersion: v1
kind: Pod
metadata:
  name: cm-test-pod
spec:
  containers:
  - name: cm-test
    image: busybox
    command: [ "/bin/sh", "-c", "env" ]
    envFrom:
    - configMapRef
       name: cm-appvars
  restartPolicy: Never
```
* 通过volumeMount使用ConfigMap,可以实现热更新
* 如果使用ConfigMap的subPath挂载为Container的Volume，Kubernetes不会做自动热更新
```
apiVersion: v1
kind: Pod
metadata:
  name: cm-test-app
spec:
  containers:
  - name: cm-test-app
    image: kubeguide/tomcat-app:v1
    ports:
    - containerPort: 8080
    volumeMounts:                   # 
    - name: serverxml               # 引用Volume的名称
      mountPath: /configfiles       # 挂载到容器的目录
  volumes:
  - name: serverxml                 # 定义Volume的名称
    configMap:
      name: cm-appconfigfiles       # 使用ConfigMap "cm-appconfigfiles"
      items:
      - key: key-serverxml          # key=key-serverxml
        path: server.xml            # value将server.xml文件名进行挂载
      - key: key-loggingproperties  # key=key-loggingpropertiesku
        path: logging.properties    # value将logging.properties文件名进行挂载
```
* ConfigMap的限制条件
  - ConfigMap文件大小限制1mb
  - ConfigMap必须在Pod之前创建
  - ConfigMap必须受Namespace限制
  - ConfigMap中的配额管理还未实现
  - Kubelet只支持可以被API server管理的Pod使用ConfigMap，静态pod无法使用
  - 在Pod对ConfigMap进行挂载(volumeMount)时，在容器内部只能挂载目录，无法挂载文件

## 3.12 Secret
* 主要作用是保管私密数据
* Secret 对象要求这些数据必须是经过Base64转码
* 生产环境需要开启Secret的加密插件
* 每个单独的Secret大小不能超过1MB
* 主要类型
  - type=Opaque
  - type=kubernetes.io/service-account-token
  - type=kubernetes.io/dockerconfigjson
  - type=boostrap.kubernetes.io/token
* kubectl create secret generic [name][DATA][TYPE]
  - docker-registry
  - generic
  - tls
  ```
  kubectl create secret generic user --from-file=./username.txt
  kubectl create secret generic pass --from-file=./password.txt
  kubectl get secrets 
  ```
* Secret的使用方式
  - 创建Pod时，通过为Pod指定Service Accout来自动使用该Secret
  - 通过挂载Secret到Pod来使用
  - 在Docker镜像下载时使用，通过指定Pod的spc.ImagePullSecrets来引用它
  ```
  apiVersion: v1
  kind: Secret
  metadata:
    name: mysecret
  type: Opaque
  data:
    password: dmFsdWUtMg0K
    username: dmFsdWUtMQ0K
  
  ---
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
    namespace: myns
  spec:
    containers:
    - name: mycontainer
      image: redis
      volumeMounts:
      - name: foo
        mountPath: "/etc/foo"
        readOnly: true
    volumes:
  ```

## 3.13 命名空间 namespace  
* 命名空间隔离的资源
  - 集群的隔离与共享
  - 同命名空间可通过主机名互相访问，但可以通过server ip, pod ip访问
  - kubectl get namespaces
  - 按环境划分: dev, test; 按团队划分; 自定义多级划分: lumine_dev
  ```
  # In a namespace
  kubectl api-resources --namespaced=true
  
  # Not in a namespace
  kubectl api-resources --namespaced=false
  ```
* 删除命名空间会附带删除该命名空间下的所以资源

# 4 Kubenetes Applications Management
## 网络访问
* k8s三层网络结构
  - 节点网络 => 集群网络 => Pod网络
* 不同网络层级的通信
  - 同一pod内多个容器的通信: local
  - 各pod间的通信: 直接通信, Overlay Network叠加网络
  - Pod与Service的通信: 直接, 通过kube-proxy来修改pod内部iptable 
* 1 集群内部
  - 1.1 DNS + ClusterIP
  - 就是一个Pod的稳定的IP地址,即VIP
  ```
  # 启动 Kubernetes proxy 模式
  $ kubectl proxy --port=8080
  curl http://localhost:8080/api/v1/proxy/namespaces/<NAMESPACE>/services/<SERVICE-NAME>:<PORT-NAME>/proxy/
  ```
  - 1.2 HeadlessService(返回具体的endpoint列表,客户端决定策略)
  - <pod-name>.<svc-name>.<namespace>.svc.cluster.local
* 2 集群内 => 集群外
  - 2.1 IP + Port
  - 2.2 OutService(空Service => 定义Endpoint)
* 3 集群外 => 集群内
  - 3.1 NodePort(Service类型,每一个物理节点都暴露一个端口) 不推荐使用
    - <任何一台宿主机的IP地址>:NodePort 访问
  - 3.2 hostport(将容器应用的端口号映射到物理机)
    hostNetwork: true
  - 3.3 通过域名访问集群内部服务
    * IngressController + Ingress
    * Config: host->api.test.com
* endpoint(类似与targetGroup)
  - 只有处于Running状态，且readinessProbe检查通过的Pod,才会出现在Service的Endpoints列表里
  ```
  kubectl get ep
  ```

## 4.1 Service
* 由Kube-proxy组件加上iptables实现
* 工作模式:
  - userspace: 1.1 ~
  - iptables: 1.10 ~
  - ipvs: 1.11+
* 负载均衡策略
  - RoundRobin(default)
  - SessionAffinity
* type
  - External name
  - ClusterIP
  - NodePort
  - LoadBalancer
* clusterIP
  - 集群内部访问
* External name
  - 用于内网访问外网资源，集群内 => 集群外
  - 创建一个无Label Slelctor的svc，手动创建和该svc同名的Endpoint来实现
  ```
  kind: Service
  apiVersion: v1
  metadata:
    name: my-service
  spec:
    ports:
    - protocol: TCP
      port: 80
      targetPort: 80
  ---------------------
  kind: Endpoints
  apiVersion: v1
  metadata:
    name: my-service
  subsets:
  - addresses:
    - IP: 1.2.3.4
    ports:
    - port: 80
      
  ```
* NodePort
  - Service类型,每一个物理节点都暴露一个端口
  - <任何一台宿主机的IP地址>:NodePort 访问
  - client => loadBlance => NodeIP:NodePort => ClusterIP:servicePort => PodIP:PodPort
  ```
  kind: Service
  apiVersion: v1
  metadata:
    name: esapi
    labels:
      app: elasticsearch
  spec:
    type: NodePort
    selector:
      app: elasticsearch
    ports:
    - protocol: TCP
      port: 9200
      targetPort: 9200
  ```
* HeadlessService
  - 返回具体的endpoint列表,客户端决定策略
  - 一般的service只把域名解析，返回一个cluster ip地址

## 4.2 Ingress
* Ingress对象，其实就是k8s对方向代理的抽象
* Ingress Controller
  - Nginx
  - HAProxy
  - Envoy
  - Traefik
* ExternalLB => Service(ingress) => IngressController => Ingress => Service => Pod
* 插件形式
```
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: cafe-ingress
spec:
  tls:
  - hosts:
    - cafe.example.com
    secretName: cafe-secret
  rules:
  - host: cafe.example.com
    http:
      paths:
      - path: /tea
        backend:
          serviceName: tea-svc
          servicePort: 80
      - path: /coffee
        backend:
          serviceName: coffee-svc
          servicePort: 80
```


# 5 Kubernetes Operations
* linux版本
  - Debian: 安定性
  - Ubuntu: 长期安定性与易用性的平衡
    - apt get 
  - Fedora: 每6个月迭代一个新版本
  - CentOS7: 事实上就是RedHat Enterprise Linux, 最流行的企业版本
    - yum install or dnf
  - openSUSE
* 推荐配置
  - Master: 4core, 16GB内存
* 最低配置
  - Master: 2core, 4GB
  - Node: 4core, 16GB
* Master与node间有大量的网络通信所
  - 配置各组件需要的通信端口号
  - 安全的内部网环境可关闭防火墙
  ```
  $ systemctl disable firewalld
  $ systemctl stop firewalld
  ```
* 禁用SELinux，让容器可以读取主机的文件系统
  ```
  setenforce 0
  ```

## 5.1 k8s的认证
* 对称加密(秘匙)与非对称加密(公私匙)
* k8s自带CA(认证局)
  - 给每个需要的组件颁发证书 
* 客户端认证(TSL双向认证)
* 认证 => 授权 => 准入控制
* 认证方式
  - BearerToken
  - SSL
  - ServiceAccount(namespace, token, ca)
  ```
  $ curl --cert userbob.pem --key userBob-key.pem \  
  --cacert /path/to/ca.pem \   
  https://k8sServer:6443/api/v1/pods
  ```
* kubectl 自带认证信息
  - .kube/config
  ```
  $ kubectl auth can-i create deployments
  yes 
  $ kubectl auth can-i create deployments --as bob
  no 
  $ kubectl auth can-i create deployments --as bob --namespace developer
  yes 
  ```

  ## 5.2 k8s的授权
* 授权模块
  - ABAC
  - webhook
  - RBAC
  - RBAC(role based access contro, k8s v1.6)
    - Authority => Role => User
* Client > API Server
* RBAC
  * User
    - User
    - ServiceAccount (集群内部访问)
  * Authority
    - Resouce + Verbs(CURD)
    - /apis/apps/v1/namespace/default/deployment/myapp-deploy/
  * Role
    - name + resouce + verbs  
  * RoleBinding
  * ClusterRole
    - ClusterRoleBinding
  * AdmisionControl
    - AlwaysAdmit
    - AlwaysDeny
    - DenyEscolatingExec

## 5.3 ServiceAccount
* 主要用来解决Pod在集群中的身份认证问题
* 通过文件目录挂载来获取交互权限 

## 5.4 kubeadm 一键部署利器
* Kubernetes一键部署利器
* 由于需要把kubelet 直接运行在宿主机上
* kops, SaltStack
    ```
    apt-get install kubeadm
    kubeadm init
    kubeadm init --config kubeadm.yaml
    ```
    
    ```kubeadm.yaml
    apiVersion: kubeadm.k8s.io/v1alpha2
    kind: MasterConfiguration
    kubernetesVersion: v1.11.0
    api:
     advertiseAddress: 192.168.0.102
     bindPort: 6443
      ...
    etcd:
     local:
     dataDir: /var/lib/etcd
     image: ""
    imageRepository: k8s.gcr.io
    kubeProxy:
     config:
     bindAddress: 0.0.0.0
        ...
    kubeletConfiguration:
     baseConfig:
     address: 0.0.0.0
        ...
    networking:
     dnsDomain: cluster.local
     podSubnet: ""
     serviceSubnet: 10.96.0.0/12
    nodeRegistration:
     criSocket: /var/run/dockershim.sock
      ...
    ```
* 附录2.2
  https://git.imooc.com/coding-335/kubernetes-ha-kubeadm

sudo su root
export KUBECONFIG=/etc/kubernetes/kubelet.conf

## 5.5  Helm
https://chartmuseum.com

## 5.6 其它工具
https://github.com/kelseyhightower/kubernetes-the-hard-way
* http://kubicorn.io/


# 6 监控与日志
## 6.1 监控
* 监控类型
  - 资源监控
  - 性能监控
  - 安全监控
  - 事件监控
* Prometheus
  -  以抓取的方式收集被监控对象的Metrics数据
  -  保存在TSDB
  - 监控体系
    - 宿主机监控数据,通过Node Exporter以DaemonSet方式运行在宿主机上获取
    - API Server、kubelet 等组件的 /metrics API
    - Kubernetes相关的监控数据(core metrics)
  - aggregator模式开启
  ```
  --requestheader-client-ca-file=<path to aggregator CA cert>
  --requestheader-allowed-names=front-proxy-client
  --requestheader-extra-headers-prefix=X-Remote-Extra-
  --requestheader-group-headers=X-Remote-Group
  --requestheader-username-headers=X-Remote-User
  --proxy-client-cert-file=<path to aggregator proxy cert>
  --proxy-client-key-file=<path to aggregator proxy key>
  ```
## 6.2 日志的采集
* 采集方式
  - 宿主机文件
  - 容器内文件
  - 容器标准/错误输出

## 6.3 Prometheus
* 以抓取的方式收集被监控对象的Metrics数据
* 保存在TSDB
* 监控体系
  - 宿主机监控数据,通过Node Exporter以DaemonSet方式运行在宿主机上获取
  - API Server、kubelet 等组件的 /metrics API
  - Kubernetes相关的监控数据(core metrics)
* aggregator模式开启
  ```
  --requestheader-client-ca-file=<path to aggregator CA cert>
  --requestheader-allowed-names=front-proxy-client
  --requestheader-extra-headers-prefix=X-Remote-Extra-
  --requestheader-group-headers=X-Remote-Group
  --requestheader-username-headers=X-Remote-User
  --proxy-client-cert-file=<path to aggregator proxy cert>
  --proxy-client-key-file=<path to aggregator proxy key>
  ```

## 6.4 Custom Metrics
* Auto Scaling的依据往往是自定义的监控指标
* 自动扩展器组件 Horizontal Pod Autoscaler( HPA )
* Aggregator APIServer
* [KubeBuilder](https://github.com/kubernetes-sigs/kubebuilder)



# 7 集群管理与维护
## 7.1 集群监控
* k8s集群健康检查api
  - curl localhost:10250/status/summary
  - curl localhost:10250/healthz
* Heapster + cAdvisor
  - kubectl top node <Node_name>

##  7.3 故障排查
* 通过kubectl describe 查看系统Event
* 通过kubectl logs 查看容器日志
  - kubectl logs <pod_name> -c <container_name>
* 查看kubernetes服务日志
  - /var/log/kubernetes
  - journalctl -u kubelet
  - kubectl logs -f kube-proxy

# 8 serverless: Knative, FaaS


