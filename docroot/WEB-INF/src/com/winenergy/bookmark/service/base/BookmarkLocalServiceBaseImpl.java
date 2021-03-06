/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.winenergy.bookmark.service.base;

import com.liferay.counter.service.CounterLocalService;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.model.PersistedModel;
import com.liferay.portal.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;

import com.winenergy.bookmark.model.Bookmark;
import com.winenergy.bookmark.service.BookmarkLocalService;
import com.winenergy.bookmark.service.persistence.BookmarkPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * The base implementation of the bookmark local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.winenergy.bookmark.service.impl.BookmarkLocalServiceImpl}.
 * </p>
 *
 * @author Fuping Ma
 * @see com.winenergy.bookmark.service.impl.BookmarkLocalServiceImpl
 * @see com.winenergy.bookmark.service.BookmarkLocalServiceUtil
 * @generated
 */
public abstract class BookmarkLocalServiceBaseImpl
	implements BookmarkLocalService, IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link com.winenergy.bookmark.service.BookmarkLocalServiceUtil} to access the bookmark local service.
	 */

	/**
	 * Adds the bookmark to the database. Also notifies the appropriate model listeners.
	 *
	 * @param bookmark the bookmark
	 * @return the bookmark that was added
	 * @throws SystemException if a system exception occurred
	 */
	public Bookmark addBookmark(Bookmark bookmark) throws SystemException {
		bookmark.setNew(true);

		bookmark = bookmarkPersistence.update(bookmark, false);

		Indexer indexer = IndexerRegistryUtil.getIndexer(getModelClassName());

		if (indexer != null) {
			try {
				indexer.reindex(bookmark);
			}
			catch (SearchException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(se, se);
				}
			}
		}

		return bookmark;
	}

	/**
	 * Creates a new bookmark with the primary key. Does not add the bookmark to the database.
	 *
	 * @param bookmarkId the primary key for the new bookmark
	 * @return the new bookmark
	 */
	public Bookmark createBookmark(long bookmarkId) {
		return bookmarkPersistence.create(bookmarkId);
	}

	/**
	 * Deletes the bookmark with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param bookmarkId the primary key of the bookmark
	 * @throws PortalException if a bookmark with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public void deleteBookmark(long bookmarkId)
		throws PortalException, SystemException {
		Bookmark bookmark = bookmarkPersistence.remove(bookmarkId);

		Indexer indexer = IndexerRegistryUtil.getIndexer(getModelClassName());

		if (indexer != null) {
			try {
				indexer.delete(bookmark);
			}
			catch (SearchException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(se, se);
				}
			}
		}
	}

	/**
	 * Deletes the bookmark from the database. Also notifies the appropriate model listeners.
	 *
	 * @param bookmark the bookmark
	 * @throws PortalException
	 * @throws SystemException if a system exception occurred
	 */
	public void deleteBookmark(Bookmark bookmark)
		throws PortalException, SystemException {
		bookmarkPersistence.remove(bookmark);

		Indexer indexer = IndexerRegistryUtil.getIndexer(getModelClassName());

		if (indexer != null) {
			try {
				indexer.delete(bookmark);
			}
			catch (SearchException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(se, se);
				}
			}
		}
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery)
		throws SystemException {
		return bookmarkPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end)
		throws SystemException {
		return bookmarkPersistence.findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 * @throws SystemException if a system exception occurred
	 */
	@SuppressWarnings("rawtypes")
	public List dynamicQuery(DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		return bookmarkPersistence.findWithDynamicQuery(dynamicQuery, start,
			end, orderByComparator);
	}

	/**
	 * Returns the number of rows that match the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows that match the dynamic query
	 * @throws SystemException if a system exception occurred
	 */
	public long dynamicQueryCount(DynamicQuery dynamicQuery)
		throws SystemException {
		return bookmarkPersistence.countWithDynamicQuery(dynamicQuery);
	}

	public Bookmark fetchBookmark(long bookmarkId) throws SystemException {
		return bookmarkPersistence.fetchByPrimaryKey(bookmarkId);
	}

	/**
	 * Returns the bookmark with the primary key.
	 *
	 * @param bookmarkId the primary key of the bookmark
	 * @return the bookmark
	 * @throws PortalException if a bookmark with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public Bookmark getBookmark(long bookmarkId)
		throws PortalException, SystemException {
		return bookmarkPersistence.findByPrimaryKey(bookmarkId);
	}

	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException, SystemException {
		return bookmarkPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the bookmarks.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of bookmarks
	 * @param end the upper bound of the range of bookmarks (not inclusive)
	 * @return the range of bookmarks
	 * @throws SystemException if a system exception occurred
	 */
	public List<Bookmark> getBookmarks(int start, int end)
		throws SystemException {
		return bookmarkPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of bookmarks.
	 *
	 * @return the number of bookmarks
	 * @throws SystemException if a system exception occurred
	 */
	public int getBookmarksCount() throws SystemException {
		return bookmarkPersistence.countAll();
	}

	/**
	 * Updates the bookmark in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param bookmark the bookmark
	 * @return the bookmark that was updated
	 * @throws SystemException if a system exception occurred
	 */
	public Bookmark updateBookmark(Bookmark bookmark) throws SystemException {
		return updateBookmark(bookmark, true);
	}

	/**
	 * Updates the bookmark in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param bookmark the bookmark
	 * @param merge whether to merge the bookmark with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
	 * @return the bookmark that was updated
	 * @throws SystemException if a system exception occurred
	 */
	public Bookmark updateBookmark(Bookmark bookmark, boolean merge)
		throws SystemException {
		bookmark.setNew(false);

		bookmark = bookmarkPersistence.update(bookmark, merge);

		Indexer indexer = IndexerRegistryUtil.getIndexer(getModelClassName());

		if (indexer != null) {
			try {
				indexer.reindex(bookmark);
			}
			catch (SearchException se) {
				if (_log.isWarnEnabled()) {
					_log.warn(se, se);
				}
			}
		}

		return bookmark;
	}

	/**
	 * Returns the bookmark local service.
	 *
	 * @return the bookmark local service
	 */
	public BookmarkLocalService getBookmarkLocalService() {
		return bookmarkLocalService;
	}

	/**
	 * Sets the bookmark local service.
	 *
	 * @param bookmarkLocalService the bookmark local service
	 */
	public void setBookmarkLocalService(
		BookmarkLocalService bookmarkLocalService) {
		this.bookmarkLocalService = bookmarkLocalService;
	}

	/**
	 * Returns the bookmark persistence.
	 *
	 * @return the bookmark persistence
	 */
	public BookmarkPersistence getBookmarkPersistence() {
		return bookmarkPersistence;
	}

	/**
	 * Sets the bookmark persistence.
	 *
	 * @param bookmarkPersistence the bookmark persistence
	 */
	public void setBookmarkPersistence(BookmarkPersistence bookmarkPersistence) {
		this.bookmarkPersistence = bookmarkPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the resource remote service.
	 *
	 * @return the resource remote service
	 */
	public ResourceService getResourceService() {
		return resourceService;
	}

	/**
	 * Sets the resource remote service.
	 *
	 * @param resourceService the resource remote service
	 */
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * Returns the resource persistence.
	 *
	 * @return the resource persistence
	 */
	public ResourcePersistence getResourcePersistence() {
		return resourcePersistence;
	}

	/**
	 * Sets the resource persistence.
	 *
	 * @param resourcePersistence the resource persistence
	 */
	public void setResourcePersistence(ResourcePersistence resourcePersistence) {
		this.resourcePersistence = resourcePersistence;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		PersistedModelLocalServiceRegistryUtil.register("com.winenergy.bookmark.model.Bookmark",
			bookmarkLocalService);
	}

	public void destroy() {
		PersistedModelLocalServiceRegistryUtil.unregister(
			"com.winenergy.bookmark.model.Bookmark");
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	protected Class<?> getModelClass() {
		return Bookmark.class;
	}

	protected String getModelClassName() {
		return Bookmark.class.getName();
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = bookmarkPersistence.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = BookmarkLocalService.class)
	protected BookmarkLocalService bookmarkLocalService;
	@BeanReference(type = BookmarkPersistence.class)
	protected BookmarkPersistence bookmarkPersistence;
	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;
	@BeanReference(type = ResourceLocalService.class)
	protected ResourceLocalService resourceLocalService;
	@BeanReference(type = ResourceService.class)
	protected ResourceService resourceService;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserLocalService.class)
	protected UserLocalService userLocalService;
	@BeanReference(type = UserService.class)
	protected UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static Log _log = LogFactoryUtil.getLog(BookmarkLocalServiceBaseImpl.class);
	private String _beanIdentifier;
}