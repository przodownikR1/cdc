import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
      method 'get'
      url '/cars'     
      headers {
        header('Content-Type', 'application/json')
      }
    }
  response {
    status 200
    body(["ford","porsche","mercedes"])
    headers {
      header('Content-Type': 'application/json')
    }
   }
  }
  