/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mailbox.inmemory.mail.task;

import static org.mockito.Mockito.mock;

import org.apache.james.dnsservice.api.DNSService;
import org.apache.james.domainlist.memory.MemoryDomainList;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.SessionProvider;
import org.apache.james.mailbox.inmemory.manager.InMemoryIntegrationResources;
import org.apache.james.mailbox.quota.CurrentQuotaManager;
import org.apache.james.mailbox.quota.UserQuotaRootResolver;
import org.apache.james.mailbox.store.mail.task.RecomputeCurrentQuotasService;
import org.apache.james.mailbox.store.mail.task.RecomputeCurrentQuotasServiceContract;
import org.apache.james.user.api.UsersRepository;
import org.apache.james.user.memory.MemoryUsersRepository;
import org.junit.jupiter.api.BeforeEach;

class MemoryRecomputeCurrentQuotasServiceTest implements RecomputeCurrentQuotasServiceContract {

    MemoryUsersRepository usersRepository;
    InMemoryIntegrationResources resources;
    RecomputeCurrentQuotasService testee;

    @BeforeEach
    void setUp() {
        DNSService dnsService = mock(DNSService.class);
        MemoryDomainList memoryDomainList = new MemoryDomainList(dnsService);
        memoryDomainList.setAutoDetect(false);
        usersRepository = MemoryUsersRepository.withoutVirtualHosting(memoryDomainList);

        resources = InMemoryIntegrationResources.defaultResources();
        testee = new RecomputeCurrentQuotasService(usersRepository, resources.getCurrentQuotaManager(), resources.getCurrentQuotaCalculator(), resources.getDefaultUserQuotaRootResolver(), resources.getMailboxManager().getSessionProvider());
    }

    @Override
    public RecomputeCurrentQuotasService testee() {
        return testee;
    }

    @Override
    public UsersRepository usersRepository() {
        return usersRepository;
    }

    @Override
    public SessionProvider sessionProvider() {
        return resources.getMailboxManager().getSessionProvider();
    }

    @Override
    public MailboxManager mailboxManager() {
        return resources.getMailboxManager();
    }

    @Override
    public CurrentQuotaManager currentQuotaManager() {
        return resources.getCurrentQuotaManager();
    }

    @Override
    public UserQuotaRootResolver userQuotaRootResolver() {
        return resources.getDefaultUserQuotaRootResolver();
    }
}