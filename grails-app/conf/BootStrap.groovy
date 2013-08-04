import com.traderapist.automation.FantasyPointProjectionScheduler
import com.traderapist.security.Role
import com.traderapist.security.User
import com.traderapist.security.UserRole

class BootStrap {

    def init = { servletContext ->

	    def adminRole = Role.findByAuthority(Role.ROLE_ADMIN)
	    def userRole = Role.findByAuthority(Role.ROLE_USER)

	    def adminUser = new User(username: "admin", password: "cfSZ9KiM", enabled: true).save(flush: true)

	    def myUser = new User(username: "dmaclean", password: "santoro821", enabled: true).save(flush: true)

	    UserRole.create adminUser, adminRole, true
	    UserRole.create adminUser, userRole, true
	    UserRole.create myUser, userRole, true

	    /*
	     * Start up the thread that monitors FantasyPointsJobs
	     */
	    Thread t = new Thread(new FantasyPointProjectionScheduler())
	    t.start()

    }
    def destroy = {
    }
}
