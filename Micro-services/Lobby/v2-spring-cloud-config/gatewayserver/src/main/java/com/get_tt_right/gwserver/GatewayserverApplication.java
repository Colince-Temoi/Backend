package com.get_tt_right.gwserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/** Update as of 12/11/2025
 * Introduction to helm and the problems that it solves
 * ----------------------------------------------------
 * # Check slides for more details.
 *
 * Installing Helm
 * -----------------
 * This we already discussed previously. To get started with helm first we need to make sure we install it inside our local system. To install this helm we need to visit the website i.e., https://helm.sh/. In the docs under introduction/Quickstart guide. Inside this page you will see a Prerequisites section to get started with helm. The very first Prerequisite is you should have a proper k8s cluster running inside your system(local system or inside any cloud environment). Post that, the next Prerequisite is we should also decide about security configurations to apply to your installation/which are related to our installation, if any - This is an optional Prerequisite and that's why you can see they highlighted, 'if any'.So, we don't need to worry about this 2nd prerequisite. The 3rd prerequisite is we need to install and configure helm inside our system. So, to install helm you can click on the LHS nav option under Docs/Introduction/Installing Helm. This will take you to the installing helm web page and inside this page we have many options on how to install helm right from Binary Releases, Script and even through Package Managers which happens to be the favourite and recommended option of my instructor haha. Here, it is very funny that we are going to install a package manager which is helm with the help of other package managers. If using macOS, then you can use homebrew which is the default package manager inside macOS. The command that we need to run for this scenario is brew install helm. With this, I am telling to my homebrew to install helm related package manager. If it was already installed inside your local system, the latest installation of helm will be pulled, if any, hence upgrading your existing helm package manager previous installation. This clearly resonates with the definition of what a package manager is i.e., A collection of software tools that automate INSTALLATION, UPGRADING, VERSION MANAGEMENT AND REMOVING COMPUTER PROGRAMMES for a computer in a consistent manner. Next, you can verify if the installation is completed or not by running the command which is helm version.This will give you an output on which helm version you are using. With this, now the helm installation is completed and you have verified.
 * Next, lets see how to install helm inside the Windows OS. For Windows OS, you can use one of the famous package managers which is Chocolatey. First, you need to make sure that Chocolatey os installed inside your system and post that only you can install helm. To install Chocolatey it si going to be supper easy, just go to their website i.e.,https://chocolatey.org/ at the top right nav you will see an instal option. If you click on it, you will be landed on the steps on how we can install this Chocolatey package manager. This is a famous package manager for Windows OS. In step 1 1. Choose How to Install Chocolatey: Select the Individual option(Reason: Because we are trying to set it up in our own local system) - actually it is selected for you by default. Next, we need to make sure we are opening the Powershell inside the Windows OS with Admin Rights. Next in your Powershell, you need to run the command which is ' Get-ExecutionPolicy' - Once you run this command you will get an output which is either AllSigned or Restricted. For some reason if you are getting the output as 'Restricted' that means you cannot install other software with the help of powershell. To disable this restricted nature, we can run a command which is Set-ExecutionPolicy AllSigned or Set-ExecutionPolicy Bypass -Scope Process. Once we execute this command, post that you can again try top run the command Get-ExecutionPolicy and you should get an output which is AllSigned or Bypass instead of Restricted. After that, it is going to be super easy, just copy the command, 'Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))' and execute it inside your powershell and this will happily install Chocolatey package manager. You can also verify whether the installation of Chocolatey is successful or not by simply running the command 'choco or choco -?' and that will give an output saying that which version of Chocolatey is installed. Once the Chocolatey is installed inside your local system, you can go to the helm site https://helm.sh/docs/intro/install, there you will see a command that you need to run i.e., choco install kubernetes-helm, in order to install helm with the help of chocolatey. As soon as you execute this command, helm is going to be installed inside your local system and you can also confirm that by running a command i.e., helm version.
 * If you are using some other OS's like Ubuntu/Linux distros in the helm site i.e., https://helm.sh/docs/intro/install, they have clearly outlined the respective approaches that you can follow to install helm. So, always follow the documentation, it has what you need anytime coz even your instructor is getting all this info you are creating your docstrings from the docs, no magic. Got any questions or doubts? The answer is in the docs.
 *
 * Installing a sample helm Chart
 * -------------------------------
 * Inside this session, we will visualize the power of helm by taking a sample Chart available form the official doc. So, before we try to explore the official doc of helm on how to install a Chart 1st we need to make sure the local k8s cluster is up and running. You can confirm this from the Docker desktop i.e., Kubernetes is running. Very similarly, you can run any of the kubectl commands. For example, I can run the command kubectl get services which will give me a list of services inside my local k8s cluster. As of now, we only have one service with the 'NAME' kubernetes which is related to k8s itself. This confirms that our local k8s cluster is working fine. As a next step, I am going to run the command i.e., helm ls. This is going to list all the releases/installations that we have done inside the k8s cluster with the help of helm. At the moment, no helm installation/release records because as of now, we have not installed any Chart into the k8s cluster. You may have a question like, how is my helm able to connect to the k8s cluster? because without connecting to the k8s cluster, my helm cannot show this output. The answer to this question is, helm is going to look for k8s cluster connection details inside your local system. So, whenever you try to connect to a k8s cluster inside your local system, it is going to make an entry into your system. For example, in my instructor's scenario, he navigated to the users/username folder. Here he looked for a hidden folder with the name .kube. Inside this, you can see a config file which if you open, you can be able to see all the connection details that my kubectl right now is using to connect to my k8s cluster. You can see that right now it is connected to a docker-desktop cluster. The same connection details also, helm is going to leverage whenever it wants to interact with the k8s cluster. Now, you should have crisp clarity. If you are using Windows OS, you will be able to find this .kube folder inside your user folder. For my case - C:\Users\pc\.kube. Next, we will explore how to set up a sample helm Chart inside our local system. This should give us some good exposure to helm and its advantages. So, inside the helm website where we explored about the installation of helm inside our local system, just under the nav link Docs/Introduction/ we have a nav link which is 'Using Helm'. Here you will find some good introduction on how to use helm, you can read if you are interested, but I want to directly go to the section labeled ''helm search': Finding Charts'. Helm comes with a powerful search command, like we said before, there is a good amount of community for helm where lot many companies, Organizations and Open Source developers, they build a lot of helm charts which you can search with the help of 'helm search'. For example, you can see a command i.e., 'helm search hub wordpress'. Think like, you want to deploy a WordPress website into your k8s cluster, for the same we need to search if there are any WordPress related charts available inside the public repositories. So in the command, 'wordpress' is the value that we are trying to search. As soon as I execute this command, you will be able to see all the repositories where we have a chart with the name 'wordpress'. In the doc, you will even see a statement like, 'The above command searches for all wordpress charts on Artifact Hub'. So, there are many repositories output that we received based on our helm search command.
 * We need to choose one of the repositories to install the wordpress helm chart. For the same, in the same webpage, if you scroll down a bit, you will see a section, ''helm install': Installing a Package'. Here they have mentioned on how to install the wordpress chart available under the bitnami repo i.e.,  helm install happy-panda bitnami/wordpress. Like we had discussed earlier on, bitnami is a famous repo which maintains production ready helm charts, so we can use the same bitnami to install this wordpress chart, but before we try to execute this command, we need to make sure that we have added the bitnami repo details inside our local system. How to add that? For this, you can just google, 'bitnami helm chart installation', you should get some results inside the Google search engine and here, you can click on the very first link which is 'Install the chart' - This page no more exists btw haha, but you need to run the same helm command that my instructor got from the page i.e.,helm repo add bitnami https://charts.bitnami.com/bitnami. Or you can be specific with your search term i.e., 'Add bitnami repository to helm' and you should be able to happily get the command that you can run in oder to achieve our requirement which is adding a bitnami repo to the local helm.By executing this command the bitnami repo should happily be added into our local system. Now, we can safely run the helm install command i.e., helm install happy-panda bitnami/wordpress. In the command, 'happy-panda' is the installation/release name that you want to give. This name can be anything. Post that 'bitnami' indicates the repository and under this repo what is the helm chart name i.e., 'wordpress'. So, on executing this command, helm is going to install the chart available inside the bitnami repo. You will also get an output saying that the installation is complete and how to access the wordpress website. In the output if you try to read the very first point, they highlighted that you can get the wordpress url by running the command shown. But before that you can see a NOTE saying that, 'It may take few minutes for the LoadBalancer IP to be available'. So, we need to be patient and give one or two minutes for this to get complete. So to understand what is the URL of wordpress, we can try to copy the entire command i.e.,
 export SERVICE_IP=$(kubectl get svc --namespace default happy-panda-wordpress --template "{{ range (index .status.loadBalancer.ingress 0) }}{{ . }}{{ end }}")
 echo "WordPress URL: http://$SERVICE_IP/"
 echo "WordPress Admin URL: http://$SERVICE_IP/admin"
 *  and execute the same inside our terminal. This will give an output of the wordpress url as well as the wordpress Admin url - If you want to log in as an admin. So you go to the browser and try to access the wordpress url i.e., http://localhost/. Woow! You should be able to see that we got a sample webpage. So, what is wordpress famous for? To build blog websites. That's why we are able to see some sample blog website. If you are not able to access it, wait for like one to 2 minutes and you should happily be able to access it. Hurreey! You can also confirm the deployment status inside the dashboard. As of now, as can be seen in the dashboard, whatever helm chart that we have installed seems it did a lot of work behind the scenes. First, you can check under the 'Deployments' nave, you will notice that there is a deployment that happened. Similarly under the 'Pods' nav link you will be able to see 2 pods. One is related to Maria DB which my blog website is going to use behind the scenes and the second pod is related to the wordpress website itself. If you click on the 'Replica Sets' link, you should be able to see replica sets details. Similarly, if you click on the 'Services' nav link that is present under 'Service' you will be able to see the services related details. For example, the service happy-panda-mariadb-headless, it is exposed as 'ClusterIP' as can be seen under the Service 'Type' column. Reason: We don't have any requirement to access it from outside the cluster as my wordpress website is going to access it internally within the cluster. Whereas, you will notice that our wordpress related service i.e., happy-panda-wordpress is deployed as 'LoadBalancer' Service 'TYPE' because we need to access it from outside the cluster and that's why we have 'LoadBalancer' Service type here. Very similarly, you can go and check what are the ConfigMaps created by clicking on the Config Maps nav link. For example, you will see a ConfigMap created i.e., happy-panda-mariadb. If you open it, you will see the various environment property details it has defined/configured. Similarly, if you click the nav link 'Secrets', you will also see we have several Secrets configured with names 'happy-panda-mariadb' - related to the db, 'happy-panda-wordpress' - related to the wordpress webiste, ...etc. So, if you try to click on 'happy-panda-wordpress' Secret, and click the eye icon, you will be able to see a Password that we can use to access the website as an admin. This way, behind the scenes my helm chart did a lot of job for me to set up this website with production ready standards. On the LHS nave side,for the nav links, you will see some with a watermark having a symbol 'N' which is to like direct you all the New stuff that have been configured/added virtue of just running the one helm command to install/deploy wordpress. Wonderfull!!! Now, let's try accessing the wordpress website as an admin. For the same use the url i.e., http://localhost/admin. It is going to ask us the username and password. Password we have already seen where it is. Alternatively we can also see the username and password from the instructions we have received on the terminal when we were installing the wordpress using the command 'helm install happy-panda bitnami/wordpress'. Under the 3rd point in the terminal output of this command execution, they have highlighted how to get the username and the password. You can try to take that command in point number 3 and run it inside your terminal as shown:
 echo Username: user
 echo Password: $(kubectl get secret --namespace default happy-panda-wordpress -o jsonpath="{.data.wordpress-password}" | base64 -d)
 *
 * With this, you should be able to get what is the username and the password that you can use to login into the admin wordpress site. Here, I can create new blogs or new articles as an admin of this wordpress website. Heurreeey! But don't forget the point we are trying to learn - did you see how easily and seamlessly am able to set up all this wordpress website with the help of helm chart!! I just run a single helm install command and behind the scenes it run so many k8s manifest files, just imagine! To see all the k8s manifest files that it executed, first we need to know where the helm chart inside your local system is saved. To understand the same we need to run the command which is 'helm env'. This will give an output about your helm. From this output, I want to highlight to you that all the helm charts are going to be saved under 'HELM_CACHE_HOME' which has a value i.e., 'C:\Users\ctemoi\AppData\Local\Temp\helm' for windows related OS's. For Mac 'AppData' is nothing but 'Caches' You can verify if this is true. So, on tyring to access this path, you will notice a folder i.e., 'repository' and here we have a helm chart with the name wordpress-27.2.0.tgz. So try to extract this compressed file. Now if you try to open this wordpress folder, you will be able to see the list of files available inside the helm chart. Next we will try to understand more details about the structure of helm chart and in the same process we are going to see all the k8s manifest files that my helm run behind the scenes to set up the wordpress website.
 *
 * Understanding helm chart structure
 * ----------------------------------
 * Check slides for more details.
 * Inside the templates/ folder we have the template files for all kind of required k8s manifest files. Since we want to do the deployment of our ms's there is a deployment related template file i.e., deployment.yaml. You can try to open the same. Inside this deployment.yaml file you can see that there is a template by following the standards of k8s manifest file for the 'kind' 'Deployment'. Hope you recall that 'Deployment' is a k8s object. Apart from the k8s manifest file syntax they are also trying to inject lot many runtime dynamic values that they have defined inside this values.yaml file. Please note that this values.yaml file will have values for all kind of template files. Apart from 'Deployment' related template file, we also have many other template files and all these template files they are going to refer for the dynamic values defined inside this values.yaml file. With all that we have discussed you should have crisp clarity about the structure of the helm Chart. As of now, as visualized we installed/deployed a 3rd party helm chart which is already readily available. For our custom ms's do you think a helm chart will be readily avavailable? haha! Of course not, we should build our own helm chart by following our own business requirements and k8s manifest files. Once we built a helm chart, we can deploy/install all our ms's using a single command. Always we just need to maintain the values.yaml files for different different environments based upon our requirements. So, as a next step, we need to create our own helm chart related to get_tt_right-bank. Before that, let me try to uninstall/undeploy the wordpress related helm chart because it is already occupying a good amount of memory inside my k8s cluster and we no more need this wordpress helm chart. Before I try to uninstall, I am going to run the command, helm ls - This is going to show you the list of installations that we have done with the help of helm i.e.,
 $ helm ls
 NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                   APP VERSION
 happy-panda     default         1               2025-11-19 08:18:09.9432378 +0300 EAT   deployed        wordpress-27.2.0        6.8.3
 *
 * Like can be seen in the output, the 'NAME' of the installation/deployment/release is going to be 'happy-panda'. And this is installed inside the 'default' 'NAMESPACE' and its 'REVISION' is '1', 'STATUS' is 'deployed' and the 'CHART' that we have used is 'wordpress-27.2.0'. Post this, we also have 'APP VERSION' i.e., 6.8.3. This App version values come from the metadata information available inside the chart.yml. Whatever app version value that we are able to see in the output above i.e., 6.8.3 , this belongs to the wordpress app. But whatever version, 27.2.0, you see as part of the output on the 'CHART' column i.e., wordpress-27.2.0 belongs to the helm chart. Here we are using the wordpress helm chart of version 27.2.0. With this helm chart we are trying to deploy a wordpress website which has a version of 6.8.3. So this 'APP VERSION' information we can maintain inside the chart.yaml. You can go and check/verify inside the chart.yaml and you should be able to see this appVersion related number. Now, I only have one release installed with the help of helm and the same I want to uninstall. The command to uninstall this is, helm uninstall <release_name> i.e., helm uninstall happy-panda. As soon as I execute this, the uninstallation will complete and now if I go check my k8s dashboard, you will be able to see that there are no workloads to display inside my dashboard. You can also confirm by clicking the navlink, 'Deployments', 'Pods', 'Replica Sets', similarly all 'Service' are also deleted/uninstalled . Also you can check and verify that the 'Config Maps' related to wordpress are also deleted/uninstalled. You can also confirm that the 'Secrets' related to wordpress are also not present. Like this, with a single helm uninstall command am able to uninstall my entire wordpress website. That's the fucking power of helm chart. We will adopt this for our mget_tt_right-bank ms's. Whatever we have discussed so far should be crisp clear.
 *
 * Creating our own helm chart and template files
 * -----------------------------------------------
 * As of now, we installed a helm chart which is available inside one of the public repositories. With the same helm chart we set up the wordpress website. But in any real projects/ in any enterprise organizations they should build their own helm charts based upon their own ms's requirements. That's why we should also try to build our own helm charts so that using the same helm charts we can deploy all our ms's into the k8s cluster with a single command and very similarly we can also uninstall with a single command and this will make our lives very easy in maintaining any number of k8s manifest files for any number of ms's that we may have inside our organization. To get started, inside our workspace location,Lobby, create a new folder 'helm'. Inside this folder only, I am going to create all the helm charts that I need for my ms's deployment. So, open the terminal at this location. As a next step, I can try to create a helm chart inside this folder. To create a helm chart we can run a command which is helm create <name_to_chart> i.e., helm create getttrightbank-common. reason for this name? haha it's because I am going to build a helm chart that is going to act as common chart for all my ms's. As soon as I execute this command a helm chart with the name getttrightbank-common will be created inside our helm folder inside our workspace location. You can validate this. You should be able to see a helm chart with all the predefined files and folders that we have din details discussed previously. So, whenever we try to create a helm chart with the help of helm create command, the helm is going to give some predefined helm chart which is going to have some content to deploy a website. Since we want to writ e our own content, what we can do here is, first we can try to delete all the templates folder contents(Template files) so that it is empty. Post that, I will also open the values.yaml file. Here you will see many values related to nginx becuase using this nginx only, the default helm chart is going to deploy a website. So, since we don't want to follow these values, you can clear all the contents of this file so that it is empty. As a next step, I am going to check in the charts folder if there are any dependencies that this default helm chart has. Actually it is empty which means that the default chart that is created by the helm does not have any dependent helm charts. So, we should be good with this empty helm chart folder. As a next step I will open this chart.yaml file. Inside this chart.yaml file, you can see the apiVersion is v2 which I am fine with, am also fine with the name, description and type as 'application', version is 0.1.0 which indicates what is the version of your helm chart. So, i am fine with this default value. The next k-v pair we have is appVersion: "1.16.0". It tells what is the version of the app you are trying to build. Since the default helm chart that is provided by the helm has some nginx website, it has a vesion which is 1.16.0. Since I want maintain my own app version, I will mention the value as 1.0.0. So, once we make these default changes, as a next step we are going to create the required template files for my getttright-bank ms's. If you can recall, in the previous sections to deploy all our ms's we have written deployment manifest files and service manifest files as well as config map. As of now, my ms's need only these manifest files to get them deployed into the k8s cluster. That's why, I am going to create 3 template files inside this helm chart which can be used by all my ms's like accounts, loans, cards and many other ms's that I have inside my ms network. For the same my instructor pasted 3 template files(deployment.yml, service.yml and configmap.yml) inside the templates folder of our helm chart which we will be discussing in details.
 * 1. service.yml
 * -------------------
 * We will discuss this first as it is simple. By looking at this yml file you can easily understand that this is a helm template but not a k8s manifest file. Reason: You are able to see all those curly braces throughout the yaml file and that is a hint for you. Below, we wil in detail understand abou this template yaml file.
 {{- define "common.service" -}} # First I am trying to define a name for this entire template which is common.service. So, with the hekp of this define function or keyword we are trying to give a name to this entire k8s template file so that other ms helm charts that we are going to build in future they can try to refer with the name common.service. So, that;s the purpose of this define keyword here. Whatever hyphens that you see at the starting and at the end are helpful to trim any spaces that you may before and after of your statement. Basically, helm is using GO template language and in few places it also uses it also uses Spring template language but we don't have to go and deep dive into the helm because we are trying to learn helm on a high level. Actually, helm itself needs a separate course to learn everything about helm. But we as developers we don't need to learn everything about helm. So, whenever you are defining a name for your template with the help of this 'define' keyword, at the end of the template file, we should also close that by using the 'end' keyword/statement as can be seen below. So, whatvere we difine/configure/mention between the define and end, that template is going to be assigned to a name which is common.service.
 apiVersion: v1 # Now inside the template the first element that we have here is the apiVersion with the value v1.
 kind: Service # And kind is 'Service'. These first 2 elements are static value and ar never going to change. That's why I am directly mentioning the k and value for them.
 metadata: # Now inside the metadata we have a name for our service.
 	name: {{ .Values.serviceName }} # Here we need to inject a dynamic value for the name of our service. So, we are using the '{{ .Values.serviceName }}' value which is defined inside the values.yaml file. And this is going to be injected at the runtime. So, whoever is going to use this template yaml file they have provide a 'serviceName' inside there values.yaml file. The same name is going to be injected here. 'values' is nothing but a helm framework object, so indie that 'values' object, the keys and value that you have defined inside your values.yml file will  and to access them I need to use the dot notation i.e., {{ .Values.serviceName }}  here serviceName is the key of the property.
 spec: # Similarly under the specification we have a selector and a type and a port.
 	selector: # Inside the selector we have a app.
 		app: {{ .Values.appLabel }} # Here we are trying to inject a dynamic value for our app label.
 	type: {{ .Values.service.type }} # Here we are trying to inject a dynamic value for the type of our service. When we will be seeing the value.yaml file then it is going to make more sense to you.
 	ports: # Under the ports we have name as http, protocol as TCP
 	  - name: http
        protocol: TCP
 		port: {{ .Values.service.port }} # Here we are trying to inject a dynamic value for the port of our service.
 		targetPort: {{ .Values.service.targetPort }} # Here we are trying to inject a dynamic value for the target port of our service.
 {{- end -}}
 *
 * 2. deployment.yml
 * -------------------
 {{- define "common.deployment" -}} # Here also we are using the same define keyword. The name that we are giving for this template is common.deployment.
 apiVersion: apps/v1 # Now inside the template the first element that we have here is the apiVersion with the value apps/v1.
 kind: Deployment # And kind is 'Deployment'.
 metadata: # Now inside the metadata we have a name for our deployment.
 	name: {{ .Values.deploymentName }} # Injecting a dynamic value for the name of our deployment.
 	labels: # Now inside the labels we have a app.
 		app: {{ .Values.appLabel }} # Injecting a dynamic value for our app label.
 spec: # Under the specification we have a replicas and a selector and a template.
 	replicas: {{ .Values.replicaCount }} # Trying to inject a dynamic value for the replica count of our deployment.
 	selector:
		 matchLabels:
 			app: {{ .Values.appLabel }} # Post that selector.matchLabels.app we are going to inject a dynamic value for our app label.
 	template:
 		metadata:
 			labels: # Now, under the template.metadata.labels.app we are going to inject a dynamic value for our app label as well.
 				app: {{ .Values.appLabel }}
 		spec:
 			containers:
 			- name: {{ .Values.appLabel }} # Under this specifications, we are trying to give a name to our container with the same value which is app label.
 		      image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}" # Post that under te image, we are trying to provide the image detailsbased upon the values which have keys like, image.repository and image.tag.
              ports:
              - containerPort: {{ .Values.containerPort }} # Under ports we are mentioning the container port.
 				protocol: TCP # And protocal is going to be TCP.
 			  env: # After defining all the conatiner related properties we should also try to inject environment variables that are required for my particular ms. That's why you can see here I have started a new element which is 'env'. Here we are trying to inject lot many properties but against each property I have an if condition. So, inside helm we can write the if checks as seen below.
 			  {{- if .Values.appname_enabled }} # So, by using this 'if.Values.appname_enabled' we are trying to check if the appName is enabled or not. So, if this boolean key i.e., appname_enabled is true, the the entire envionment valiable defined under this condition, just before {{- end }} is going to be injected into the particular ms deployment yaml file.
 			  - name: SPRING_APPLICATION_NAME # For this environment property,
  				value: {{ .Values.appName }} # We are trying to assign the value using the appName which is a key defined in the values.yaml file. I am not trying to inject this using the config map reason being the value to this key is going to vary/be defiied for each and every ms. That's why we are directly mentioning the value/name inside the values.yaml file of the particular ms.
  			  {{- end }} # Each if check has to end with this end statement. The reason I have written this if check is because, maybe there may be some ms where I don't need to pass the property which is SPRING_APPLICATION_NAME. So, if the value of this key is empty, then we don't need to inject this environment variable hence this if block/condition. So, for such applications, we can have the boolean appname_enabled as false inside the values.yaml and with that this environment will not be injected into the k8s manifest file. With this explanation, you shold now be having crisp clarity.
 			  {{- if .Values.profile_enabled }} # Post that I am using one more if condition which is if the profile is enabled.
 			  - name: SPRING_PROFILES_ACTIVE # For some ms's we want to inject the environment variable which is  SPRING_PROFILES_ACTIVE. So, using this environment property we can tell to the Spring boot application whether we want to start our application with the prod profile or qa or default profile. That's why I have mentioned this profile_enabled condition. I mean if true then only this environment variable is going to be injected into the k8s manifest file.
  				valueFrom:
 			 		configMapKeyRef:
  						name: {{ .Values.global.configMapName }} # The value of this environment varaible I am going to get it from the configMap with the name that I have mentioned in the values.yaml file i.e., Values.global.configMapName
  						key: SPRING_PROFILES_ACTIVE # The key inside the configMap that I am going to get the value from. So, based upon this key inside the configMap, my helm is going to lookup for the value and the same is going to be assigned to this, SPRING_PROFILES_ACTIVE environment property/variable.
  			  {{- end }}
   			  {{- if .Values.config_enabled }} # Very similarly if the config_enabled is true, then only this environment property/variable , SPRING_CONFIG_IMPORT, is going to be injected into the k8s manifest file.
 			  - name: SPRING_CONFIG_IMPORT
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: SPRING_CONFIG_IMPORT
  			  {{- end }}
 			  {{- if .Values.eureka_enabled }} # Based upon this if condition, if the eureka_enabled is true, then only this environment property, EUREKA_CLIENT_SERVICEURL_DEFAULTZONE, is going to be injected into the k8s manifest file.
 			  - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
 			  {{- end }}
 			  {{- if .Values.resouceserver_enabled }} # At last based on this if condition, if the resouceserver_enabled is true, then only this environment property, SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI, is going to be injected into the k8s manifest file. This means that if my microservice is acting as respurce server inside OAuth2, which is goinng to be the scenario for gatewayserver, then we are going to inject this environment property/variable into the k8s manifest file. For all the remaining ms's other than gatewayserver, this boolean is going to be false and with that we are not going to inject this environment variable/property in all other remaining ms's except the gatewayserver.
 			  - name: SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI
 			  {{- end }}
			  {{- if .Values.otel_enabled }} # At last since we are going to set up the entire ms network along with Grafana,Open Telemetry for the same we need to inject some properties, like what is the location where our Open Telemetry jar is available. The same I am going to inject with this JAVA_TOOL_OPTIONS environment property. This property is going to be injected only if the otel_enabled is true for a particular ms.
 			  - name: JAVA_TOOL_OPTIONS
 				valueFrom:
					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: JAVA_TOOL_OPTIONS
 			  - name: OTEL_EXPORTER_OTLP_ENDPOINT # If the same otel_enbaled is true, I am also trying to inject other environment variables like what is the OTEL_EXPORTER_OTLP_ENDPOINT - to which we need to provide what is the endpoints url where my Open Telemetry can send the details. As per recent versions of Opentelemetry,we should also take care of OTEL_LOGS_EXPORTER.
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: OTEL_EXPORTER_OTLP_ENDPOINT
 			  - name: OTEL_METRICS_EXPORTER # Here we also need to inject the OTEL_METRICS_EXPORTER environment variable.
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: OTEL_METRICS_EXPORTER
 			  - name: OTEL_LOGS_EXPORTER # As per recent versions of Opentelemetry,we should also take care of OTEL_LOGS_EXPORTER.
 				valueFrom:
 					configMapKeyRef:
 					name: {{ .Values.global.configMapName }}
 					key: OTEL_LOGS_EXPORTER
 			  - name: OTEL_SERVICE_NAME # At last we also need to inject the OTEL_SERVICE_NAME environment variable. All these Opentelemetry related environment variable that we have injected along with the JAVA_TOOL_OPTIONS environment variable, are going to be injected only if the otel_enabled is true for a particular ms. All these we discussed in details inside the Observability and Monitoring sessions. You should be having crisp clarity about them, if you have some questions you can look at the docker compose files and the respective docstrings where we were discussing all these for a refresher. With that you should be able to understand why we are injecting all these properties.
 				value: {{ .Values.appName }}
 			  {{- end }}
 			  {{- if .Values.kafka_enabled }} # Here we have one more if condition, if the kafka_enabled is true, then only this environment property, SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS, is going to be injected into the k8s manifest file. Since we are going to visualize the complete demo of complete ms's set-up, inside this sessions, we will be leveraging kafka to demo on event driven ms's. For the same like you know the accounts ms and the message ms, they are going to connect with the kafka broker. So, if the kafka_enabled is true, then only this environment property/variable is going to be injected into the k8s manifest files for the respective ms's i.e., accounts and message ms.
 			  - name: SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS
 				valueFrom:
 					configMapKeyRef:
 						name: {{ .Values.global.configMapName }}
 						key: SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS
 			  {{- end }}

 {{- end -}}

 * So, that was the complete-detailed explanation breakdown of the deployment.yaml template file
 * 3. configmap.yaml
 * ------------------
 {{- define "common.configmap" -}} # First I am trying to give a name,common.configmap, to this configmap template file.
 apiVersion: v1 # The apiVersion is going to be v1
 kind: ConfigMap # The kind is going to be ConfigMap
 metadata:
 	name: {{ .Values.global.configMapName }} # The name I am trying to give from the Values.global.configMapName. Like you have been seeing, I am going to follow a standard, nothing but whenever I mention the prefix value as 'global', then that property name is going to be common for all ms's inside my ms network. So, the configMapName is going to be common for all ms's and that's why you can see I am mentioning 'global' as a prefix.
 data:
 	SPRING_PROFILES_ACTIVE: {{ .Values.global.activeProfile }} # Very similarly,SPRING_PROFILES_ACTIVE, I am going to read from the Values object with the key as global.activeProfile. So, the activeProfile is going to be common for all ms's.
 	SPRING_CONFIG_IMPORT: {{ .Values.global.configServerURL }} # Same for Spring Config support
 	EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: {{ .Values.global.eurekaServerURL }} # Same for Eureka Service Url
 	SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK-SET-URI: {{ .Values.global.keyCloakURL }} # Very similarly what is the Spring security resource server url where my gatewayserver can get a certificate of my KeyCloak. So, those details also I am going to provide with the help of this Values.global.keyCloakURL.
 	JAVA_TOOL_OPTIONS: {{ .Values.global.openTelemetryJavaAgent }} # With the help of this JAVA_TOOL_OPTIONS, I am going to mention where is the path of Opentelemetry Java Agent.
 	OTEL_EXPORTER_OTLP_ENDPOINT: {{ .Values.global.otelExporterEndPoint }}
 	OTEL_METRICS_EXPORTER: {{ .Values.global.otelMetricsExporter }}
 	OTEL_LOGS_EXPORTER: {{ .Values.global.otelLogsExporter }}
 	SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS: {{ .Values.global.kafkaBrokerURL }}
 {{- end -}}
 *
 *  If you see above in the configmap.yaml template file, I am trying to create all the environment varaibles/properties that we have mentioned/used in the deployment.yaml template file. But I am not trying to mention the direct hardcoded values inside the configmap.yaml template file. Reason: I may have different different requirements like for dev environment I may have differemt profile, different urls and similarly for qa and pdn environments, we may have different values. So, that's why we are going to maintain values.yaml for different different environments but the template is going to be the same and that's why I am trying to mention these variable names instead of hardcoded values inside this template file.
 *  So far, you should have crisp clarity about these 3 template file that we have defined/written inside the getttrightbank-common helm chart. As a next step, we can cross-check the values.yaml file and make sure that this is empty Reason: This getttrightbank-common helm chart is going to be levaraged by other helm charts. So, whoever is going to levergae this common helm chart i.e., getttrightbank-common, they are going to provide their own values.yaml file and this helm chart which is getttrightbank-common does not have any specific requirements to pass it's own values and that's why we are going to keep the values.yaml file for this helm chart as empty. So with this getttrightbank-common helm chart, we have prepared a common helm chart which has the template files related to service, deploymnt and configmap. As a next step, we need to create the helm charts which can levarage these templates and provide their own values.yaml files based upon the specific ms requirements. That is what we will be doing next but so far you should be super clear with what we have been discussing.
 *
 * Creating helm chart for Accounts ms
 * ------------------------------------
 * We will see how to create a helm chart for our accounts ms by leveraging the getttrightbank-common helm chart because inside this getttrightbank-common helm chart only, we have defined all the required k8s manifest files. So, my accounts ms has to use these templates andd post that it should also mention the values inside the values.yaml file. For the same, in the location where we have getttrightbank-common folder, I am going to create a new folder i.e. getttrightbank-services. Because inside this folder I am going to create all the helm chart that are required for my ms's. Open this folder from your terminal i.e., Git Bash. Inside this folder only we are goingt o create a new helm chart with the name 'accounts'. For the same, run the command helm create accounts. Now, in your Windows file explorer you can navigate to this newly created helm chart and like we discussed before, delete all the templates that we received from the default helm chart. With this the templates folder should have nothing. Very similarly, I am going to delete the values.yaml file content such that it is empty. Post that, open the chart.yaml of accounts helm chart. Inside this chart.yaml, I am fine with the apiVersion, name, description, type, version values. For the appVersion value i.e., change it to "1.0.0".
 * Now, inside the same chart.yaml, file, I need to define if this helm chart has any dependency on other helm charts. We know that this accounts ms related helm chart has a dependency on getttrightbank-common helm chart. So, how to define those details? appVersion: "1.0.0" mention a new element which is "dependencies" as shown below:
 dependencies: # Inside this dependencies element we can mention the list of helm charts that this helm chart has a dependency on
   - name: getttrightbank-common # First here we have to mention the name of the helm chart that this helm chart has a dependency on
 	 version: 0.1.0 # Post that here we have to mention the version of the helm chart that this helm chart has a dependency on which is nothing but getttrightbank-common helm chart version. This value you can get it from the getttrightbank-common helm chart chart.yaml file.
 	 repository: file://../../getttrightbank-common # After the version we need to mention the repository element to which we need to provide a value as the path where getttrightbank-common helm chart is present. So, if your helm chart is available inside a url, then you can mention directly the url details. But right now since it is availbale inside my local system, I need to mention this file://../../getttrightbank-common. The reason I am mentioning this '../' twice means nothing but from this location where this accounts related helm chart chart.yaml file is present the getttrightbank-common helm chart is going to be available 2 folders outside. That's it, nothing fancy! haha. Once we have defined these dependencies we should be good. As a next step, I need to provide the templates inside the accounts helm chart. Right now it is empty. You may have a question like, but we already defined the templates inside getttrightbank-common helm chart, why do we need to define them inside accounts helm chart again? We will get to know in a few. So inside this templates folder of accounts helm chart, have pasted 2 template files i.e., deployment.yaml and service.yaml. To deploy my accounts ms, I just need to apply the deployment.yaml and service.yaml manifest files. That's it. I don't have any other requirements, If you have other requirements where you have to define other k8s objects then definately you need to define the template files specific to those k8s objects. If you try to open these 2 yaml files and try to understand what is there, you will get your answer! hahaha. Inside deployment.yaml file you will see {{- template "common.deployment" . -}} as the only content present which means nothing but here, I am just trying to refer to another template that I have defined with the name 'common.deployment'. Do you recal where we have defined this template? haha -Inside the getttrightbank-common helm chart, we have defined this template with the name 'common.deployment'  So, I am simply trying to refer to another template available inside another helm chart which is getttrightbank-common. This is nothing but a mechanism to reuse templates from one helm chart to another helm chart. That's it! That's why first we need to make sure we added the getttrightbank-common helm chart as a dependency to this accounts helm chart. And post that only we should be able to use this ,{{- template "common.deployment" . -}} template. So the syntaxt is, 'template' keyword followed by what is the name of the template. Also make sure that the dot is also present after the template name as can be seen because this is the syntax that we need to follow. Very similarly, If I go and observe the service.yaml template file you will see, {{- template "common.service" . -}}. This is just the same kind of setup that we have just discuused like in the case of deployment.yaml file. of accounts related helm chart. Here I am trying to refer to another template with the name common.service. With this any doubts to the qwuestion you were asking yourself earlier should be crisply be answered.
 *
 * As a next step we need to populate all the required values inside thevalues.yaml file of accounts ms. For the same, open the values .yaml file, as of now it is empty. Here, I have paste the belwo propert
 # This is a YAML-formatted file.
 # Declare variables to be passed into your templates.

 deploymentName: accounts-deployment # Here, first I am trying to mention a key with the name ,'deploymentName' and the value of this key is accounts-deployment. If you try to open the deployment.yaml file that we have written inside the getttrightbank-common then you should be able to understand. There you will see something like, {{ .Values.deploymentName }}. So, here, I am trying to refer to the value of this key 'deploymentName' and the value of this key is accounts-deployment.
 serviceName: accounts # Very similarly, I am trying to mention what is the serviceName.
 appLabel: accounts # What is the appLabel
 appName: accounts # What is the appName

 replicaCount: 1 # What is the replicaCount

 image:
 	repository: colince819/accounts # Under the image.repository I am trying to mention what is the image name that my template has to consider for accounts ms.
	 tag: v9 # And What is the tag name. Here we are deploying all the code from Section 14 where we have implemented that why we are trying to use the tag v9 in my case.

 containerPort: 8080 # Post that under the containerPort I have mentioned the value 8080.

 service:
 	type: ClusterIP # The Service type here I am mentioning 'ClustreIP' because I don't want to expose my accounts ms to the outside world. I only want to restrict my gatewayserver OR other internal ms's within the cluster ONLY can communicate with my accounts ms. With that reason the Service type I am going to mention it as 'ClusterIP'. So, whatvere value we have mentioned here this will be mapped to the service.yaml file that we have defined inside the templates folder of getttrightbank-common helm chart. If you open it you will see a variable with the name {{ .Values.service.type }}. The same will be reffered from the values.yaml file which we have defined inside the accounts ms specific helm chart.
 	port: 8080 # Service.port I am mentioning as  8080.
 	targetPort: 8080 # And Service.targetPort as 8080. Reason: We want to start our accounts ms at the port 8080.

 # Below you can see I have defined some variable and to these variables I am mentioning some boolean values. So based on these variables only, all the environment varaibles that we have defined inside our deploymemt.yaml template file will be injected.
 appname_enabled: true # For eaxmple, for accounts ms related manifest file, I want to inject what is the Spring application name property and that's why I am mentioning this appname_enabled as true. If you check in your deployment.yaml template file, whenever this appname_enabled is true, then the environment variable called 'SPRING_APPLICATION_NAME' will be injected into the manifest file. Nothing but an application's/service's environment variable/property with the name SPRING_APPLICATION_NAME will be created and for this, the value will be picked from the values.yaml file i.e., {{ .Values.appName }}
 profile_enabled: true # Very similarly I also mentioned profile_enabled as true.
 config_enabled: true # config_enabled as true. Reason: My accounts ms has to connect and read the properties from the config server. That's why I am mentioning this as true.
 eureka_enabled: true # Very similarly I am also mentioning this eureka_enabled as true. Reason: My accounts ms has to connect with the Eureka server. That's why I am mentioning this as true.
 resouceserver_enabled: false # Resource Server enabled as false : Reason: My accounts ms is not a resource server. That's why I am mentioning this as false. Inside my ms's, only gateway server is going to act as a resource server - It's the only one that is going to need the property related to KeyCloak URL
 otel_enabled: true # Opentelemetry enabled as true because we want our accounts ms to send the logs to the Grafana and the distributed tracing details also to the Grafana Tempo component. With all these reasons I have mentioned the otel_enabled as true.
 kafka_enabled: true # Kafka enabled as true because we want our accounts ms to connect with the kafka broker so that it can communicate with the message ms asynchronously with the help of event driven pattern. That's why I am mentioning this as true.
 *
 * So, with this, we have defined all the required details inside the accounts helm chart. We have populated the values.yaml file and under the templates we have pasted required templates. We have also updated the chart.yaml file with the required dependencies. But you can notice that the chart folder is empty. Previously, we said all the dependent helm charts will be available inside this chart folder. Right now it is empty because we have not compiled all our required helm charts as of now. For that reason we are not able to see the dependent helm chart details inside the accounts ms. As a next step, we will try to compile any dependent helm charts. So, inside the accounts related helm chart open your terminal i.e., D:\Software Downloads\Spring Boot\Spring\SPRING Programming Tutorial  by Nagoor Babu Sir On 15-02-2019\VCS-Backend\Backend\Micro-services\Lobby\helm\getttrightbank-services\accounts. Here I am going to run the command helm dependencies build. This command will compile my accounts helm chart and it will also try to compile all the dependent hel charts and place them inside the chart folder.  You can see we got an output like below:
 $ helm dependencies build
 Hang tight while we grab the latest from your chart repositories...
 ...Successfully got an update from the "kubernetes-dashboard" chart repository
 Update Complete. ⎈Happy Helming!⎈
 Saving 1 charts
 Deleting outdated charts
 *
 * This indicates that the compilation is successful. Now, if you go to the account related helm chart, in the charts folder that was initially empty you can be able to validate that there will be a compressed helm chart 'getttrightbank-common' and the version of the helm chart '0.1.0' i.e., getttrightbank-common-0.1.0.tgz. So, this is how the dependent helm charts are going to come into your own helm chart. So, always make sure that you are running the helm dependencies build command whenever your helm chart has dependencies on other helm charts. For getttrightbank-common helm chart, there is no dependency on any other helm chart. That's why we didn't run the helm dependencies build command for it. With this you should be super clear on how to create a helm chart for a specific ms. As a next step, we need to create helm chart for the remaining ms's as well.
 * * */
@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/* This method is going to create a bean of type RouteLocator and return it.
	* Inside this RouteLocator only, we are going to send all our custom routing related configurations based on our requirements.
	**/
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p.path("/eazybank/accounts/**")
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS"))

				.route(p -> p.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("lb://LOANS"))
				.route(p -> p.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time",LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())))
						.uri("lb://CARDS")).build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 1, 1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
