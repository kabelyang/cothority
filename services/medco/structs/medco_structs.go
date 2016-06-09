package medco_structs

import (
	"github.com/btcsuite/goleveldb/leveldb/errors"
)

const MAX_GROUP_ATTR int = 2  //we must have this limit because slices cannot be used as keys in maps
type GroupingAttributes [MAX_GROUP_ATTR]DeterministCipherText
type TempID uint64

type ClientResponse struct {
	ProbabilisticGroupingAttributes CipherVector
	AggregatingAttributes CipherVector
}


func DeterministicCipherVectorToGroupingAttributes(attrs DeterministCipherVector) (GroupingAttributes,error) {
	var groupingAttributes GroupingAttributes

	if len(attrs) > MAX_GROUP_ATTR {
		return groupingAttributes, errors.New("Deterministic Cipher Vector too big to be Grouping Attributes")
	}

	for i, attr := range attrs {
		groupingAttributes[i] = attr
	}

	return groupingAttributes,nil
}

func GroupingAttributesToDeterministicCipherVector(groupingAttrs GroupingAttributes) DeterministCipherVector {
	return groupingAttrs[:]
}