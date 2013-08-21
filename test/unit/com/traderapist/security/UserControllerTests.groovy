package com.traderapist.security

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.After
import org.junit.Before

@TestFor(UserController)
@Mock([Role,User,UserRole])
class UserControllerTests {

	Role roleUser
	Role roleAdmin

	@Before
	void setUp() {
		roleUser = new Role(authority: Role.ROLE_USER).save(flush: true)
		roleAdmin = new Role(authority: Role.ROLE_ADMIN).save(flush: true)
	}

	@After
	void tearDown() {
		roleUser = null
		roleAdmin = null
	}

    def populateValidParams(params) {
        assert params != null
        params["username"] = "dan@traderapist.com"
	    params["password"] = "password"
    }

    void testIndex() {
        controller.index()
        assert "/user/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.userInstanceList.size() == 0
        assert model.userInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.userInstance != null
    }

    void testSave() {
	    User.metaClass.encodePassword = { -> }

	    assert UserRole.list().size() == 0

        controller.save()

        assert model.userInstance != null
        assert view == '/user/create'

        response.reset()

        populateValidParams(params)
        controller.save()

	    def userRoles = UserRole.list()
	    assert userRoles.size() == 1
	    assert userRoles[0].role == roleUser
	    assert userRoles[0].user.username == "dan@traderapist.com"
		assert userRoles[0].user.enabled

        assert response.redirectedUrl == '/home/index'
        assert controller.flash.info != null
        assert User.count() == 1
    }

	void testSave_InvalidEmail() {
		User.metaClass.encodePassword = { -> }

		params.username = "bademail"
		params.password = "password"

		controller.save()

		assert model.userInstance != null
		assert view == '/user/create'

		response.reset()

		populateValidParams(params)
		controller.save()

		assert response.redirectedUrl == '/home/index'
		assert controller.flash.info != null
		assert User.count() == 1
	}

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/user/list'

        populateValidParams(params)
        def user = new User(params)

        assert user.save() != null

        params.id = user.id

        def model = controller.show()

        assert model.userInstance == user
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/user/list'

        populateValidParams(params)
        def user = new User(params)

        assert user.save() != null

        params.id = user.id

        def model = controller.edit()

        assert model.userInstance == user
    }

    void testUpdate() {
	    User.metaClass.encodePassword = { -> }

        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/user/list'

        response.reset()

        populateValidParams(params)
        def user = new User(params)

        assert user.save() != null

        // test invalid parameters in update
        params.id = user.id
	    params["username"] = ""

        controller.update()

        assert view == "/user/edit"
        assert model.userInstance != null

        user.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/user/show/$user.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        user.clearErrors()

        populateValidParams(params)
        params.id = user.id
        params.version = -1
        controller.update()

        assert view == "/user/edit"
        assert model.userInstance != null
        assert model.userInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
	    User.metaClass.encodePassword = { -> }

        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/user/list'

        response.reset()

        populateValidParams(params)
        def user = new User(params)

        assert user.save() != null
        assert User.count() == 1

        params.id = user.id

        controller.delete()

        assert User.count() == 0
        assert User.get(user.id) == null
        assert response.redirectedUrl == '/user/list'
    }
}
