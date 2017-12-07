package guard

import (
	"github.com/dedis/cothority"
	"github.com/dedis/kyber"
	"github.com/dedis/onet"
	"github.com/dedis/onet/log"
	"github.com/dedis/onet/network"
)

// Client is a structure to communicate with Guard service
type Client struct {
	*onet.Client
}

// NewClient makes a new Client
func NewClient() *Client {
	return &Client{Client: onet.NewClient(ServiceName, cothority.Suite)}
}

// SendToGuard is the function that sends a request to the guard server from the client and receives the responses
func (c *Client) SendToGuard(dst *network.ServerIdentity, UID []byte, epoch []byte, t kyber.Point) (*Response, onet.ClientError) {
	//send request an entity in the network
	log.Lvl4("Sending Request to ", dst)
	serviceReq := &Request{UID, epoch, t}
	reply := &Response{}
	cerr := c.SendProtobuf(dst, serviceReq, reply)
	if cerr != nil {
		return nil, cerr
	}
	return reply, nil
}
