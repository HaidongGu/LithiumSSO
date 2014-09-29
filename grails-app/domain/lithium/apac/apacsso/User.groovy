package lithium.apac.apacsso

class User {

	transient springSecurityService

	String username
	String password
	String email
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	String screenname

	String registrationSource = 'web'
	Date verifiedDate 


	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email blank: false, email: true
		screenname blank: false, unique: true, size: 3..15
		verifiedDate nullable: true
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
		if (!screenname) {
			screenname = username
		}
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
		if (!screenname) {
			screenname = username
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
