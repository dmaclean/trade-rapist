dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            //dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            dbCreate=""
            url = "jdbc:mysql://localhost/fantasy_yahoo"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "fantasy"
            password = "fantasy"
            logSql=false
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            logSql=true
        }
    }
    production {
        dataSource {
//            dbCreate = "update"
//            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
//            pooled = true
//            properties {
//               maxActive = -1
//               minEvictableIdleTimeMillis=1800000
//               timeBetweenEvictionRunsMillis=1800000
//               numTestsPerEvictionRun=3
//               testOnBorrow=true
//               testWhileIdle=true
//               testOnReturn=true
//               validationQuery="SELECT 1"
//            }
	        url = "jdbc:mysql://aa1cx34u4zps7mj.cz4klpvd9rfo.us-east-1.rds.amazonaws.com:3306/ebdb"
	        driverClassName = "com.mysql.jdbc.Driver"
	        username = "traderapist"
	        password = "santoro821"
	        logSql=false
        }
    }
}
