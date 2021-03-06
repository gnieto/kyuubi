/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yaooqinn.kyuubi.auth

import org.apache.hadoop.hive.thrift.HadoopThriftAuthBridge.Server
import org.apache.hive.service.cli.thrift.TCLIService.{Iface, Processor}
import org.apache.thrift.TProcessor
import org.apache.thrift.TProcessorFactory
import org.apache.thrift.transport.TTransport

object KerberosSaslHelper {

  def getProcessorFactory(saslServer: Server, service: Iface): TProcessorFactory =
    CLIServiceProcessorFactory(saslServer, service)

  case class CLIServiceProcessorFactory(saslServer: Server, service: Iface)
    extends TProcessorFactory(null) {
    override def getProcessor(trans: TTransport): TProcessor = {
      val sqlProcessor = new Processor[Iface](service)
      saslServer.wrapNonAssumingProcessor(sqlProcessor)
    }
  }
}