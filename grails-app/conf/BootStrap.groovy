import lithium.apac.apacsso.*

class BootStrap {

    def init = { servletContext ->
        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(flush: true, failOnError: true)
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(flush: true, failOnError: true)

        def adminUser = User.findByUsername('admin')
        if (!adminUser) {
            adminUser = new User(username: 'admin', enabled: true, password: 'admin123', email: 'admin@email.apacsso.com', screenname: 'adminSN' ).save(flush: true, failOnError: true)  
        }
        def normalUser = User.findByUsername('user001')
        if (!normalUser) {
            normalUser = new User(username: 'user001', enabled: true, password: 'user321', email: 'user001@email.apacsso.com', screenname: 'user001SN' ).save(flush: true, failOnError: true)  
        }

        if (!UserRole.get(adminUser.id, userRole.id)) {
            UserRole.create adminUser, userRole, true
        }
        if (!UserRole.get(adminUser.id, adminRole.id)) {
            UserRole.create adminUser, adminRole, true
        } 

        if (!UserRole.get(normalUser.id, userRole.id)) {
            UserRole.create normalUser, userRole, true
        }

        assert User.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 3
    }
    def destroy = {
    }
}
